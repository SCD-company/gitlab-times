package com.scd.gitlabtimeback.filter;

import com.google.common.collect.Lists;
import com.scd.gitlabtimeback.Role.UserAdmin;
import com.scd.gitlabtimeback.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.filter.OAuth2AuthenticationFailureEvent;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.util.Assert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class AuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public OAuth2RestOperations restTemplate;
    private ResourceServerTokenServices tokenServices;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();
    private ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    public void setTokenServices(ResourceServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        super.setApplicationEventPublisher(eventPublisher);
    }

    public AuthenticationProcessingFilter(@Autowired UserService userService) {
        super("/auth/login");
        this.userService = userService;
        setAuthenticationManager(new NoopAuthenticationManager());
        setAuthenticationDetailsSource(authenticationDetailsSource);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(restTemplate != null, "Supply a rest-template");
        super.afterPropertiesSet();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        OAuth2AccessToken accessToken;
        try {
            accessToken = restTemplate.getAccessToken();
        } catch (OAuth2Exception e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain access token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }
        try {

            OAuth2Authentication tmp = tokenServices.loadAuthentication(accessToken.getValue());

            @SuppressWarnings("unchecked")
            Long userId = Long.valueOf((Integer) ((Map<String, Object>) (tmp.getUserAuthentication().getDetails())).get("id"));

            OAuth2Authentication result = null;

            if (authenticationDetailsSource != null) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        tmp.getUserAuthentication().getPrincipal(),
                        tmp.getUserAuthentication().getCredentials(),
                        Lists.newArrayList(new UserAdmin(userService, userId))
                );
                usernamePasswordAuthenticationToken.setDetails(tmp.getUserAuthentication().getDetails());

                OAuth2Request oAuth2Request = new OAuth2Request(
                        tmp.getOAuth2Request().getRequestParameters(),
                        tmp.getOAuth2Request().getClientId(),
                        Lists.newArrayList(new UserAdmin(userService, userId)),
                        tmp.getOAuth2Request().isApproved(),
                        tmp.getOAuth2Request().getScope(),
                        tmp.getOAuth2Request().getResourceIds(),
                        tmp.getOAuth2Request().getRedirectUri(),
                        tmp.getOAuth2Request().getResponseTypes(),
                        tmp.getOAuth2Request().getExtensions()
                );


                result = new OAuth2Authentication(
                        oAuth2Request,
                        usernamePasswordAuthenticationToken);

                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, accessToken.getValue());
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, accessToken.getTokenType());
                result.setDetails(authenticationDetailsSource.buildDetails(request));
            }
            assert result != null;
            publish(new AuthenticationSuccessEvent(result));
            return result;
        } catch (InvalidTokenException e) {
            BadCredentialsException bad = new BadCredentialsException("Could not obtain user details from token", e);
            publish(new OAuth2AuthenticationFailureEvent(bad));
            throw bad;
        }

    }

    private void publish(ApplicationEvent event) {
        if (eventPublisher != null) {
            eventPublisher.publishEvent(event);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        restTemplate.getAccessToken();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        if (failed instanceof AccessTokenRequiredException) {
            throw failed;
        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }

    private static class NoopAuthenticationManager implements AuthenticationManager {

        @Override
        public Authentication authenticate(Authentication authentication)
                throws AuthenticationException {
            throw new UnsupportedOperationException("No authentication should be done with this AuthenticationManager");
        }

    }

}