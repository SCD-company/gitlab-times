package com.scd.gitlabtimeback.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.scd.gitlabtimeback.filter.AuthenticationProcessingFilter;
import com.scd.gitlabtimeback.service.UserService;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.securityEnabled}")
    private boolean securityEnabled;

    @Value("${app.serverDomain}")
    private String serverDomain;

    @Value("${app.applicationDomain}")
    private String applicationDomain;

    @Value("${app.protocol}")
    private String serverProtocol;

    private final OAuth2ClientContext oauth2ClientContext;
    private final UserService userService;


    public SecurityConfig(@Autowired OAuth2ClientContext oauth2ClientContext, @Autowired UserService userService) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.userService = userService;
    }

    @Bean
    public Filter shallowETagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.logout()
                .logoutSuccessUrl(serverProtocol + "://" + applicationDomain + "/login")
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID");


        http.requestMatchers()
                .and()
                .antMatcher("/**")
                .headers()
                .cacheControl()
                .disable();

        if (securityEnabled) {
            http.requestMatchers()
                    .and()
                    .authorizeRequests().antMatchers( "/api/user", "/api/report**","/api/options").authenticated()
                    .and()
                    .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);

          
        }
    }

    private Filter ssoFilter() {
        AuthenticationProcessingFilter gitlabFilter = new AuthenticationProcessingFilter(userService);
        OAuth2RestTemplate gitlabTemplate = new OAuth2RestTemplate(gitlab(), oauth2ClientContext);
        gitlabFilter.setRestTemplate(gitlabTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(gitlabResource().getUserInfoUri(), gitlab().getClientId());

        tokenServices.setRestTemplate(gitlabTemplate);
        gitlabFilter.setTokenServices(tokenServices);
        gitlabFilter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler() {
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                this.setDefaultTargetUrl(serverProtocol + "://" + applicationDomain + "/report");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        });
        gitlabFilter.setAuthenticationFailureHandler(
                new SimpleUrlAuthenticationFailureHandler() {
                    public void onAuthenticationFailure(
                            HttpServletRequest request,
                            HttpServletResponse response,
                            AuthenticationException exception
                    ) throws IOException, ServletException {
                        this.setDefaultFailureUrl(serverProtocol + "://" + applicationDomain + "/login?status=fail");
                        super.onAuthenticationFailure(request, response, exception);
                    }
                }
        );
        return gitlabFilter;
    }


    @Bean
    @ConfigurationProperties("gitlab.client")
    public AuthorizationCodeResourceDetails gitlab() {
        AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setPreEstablishedRedirectUri(serverProtocol + "://" + serverDomain + "/auth/login");
        return details;
    }

    @Bean
    @ConfigurationProperties("gitlab.resource")
    public ResourceServerProperties gitlabResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean<?> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }
}
