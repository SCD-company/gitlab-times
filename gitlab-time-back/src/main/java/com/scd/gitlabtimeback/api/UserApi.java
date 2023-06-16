package com.scd.gitlabtimeback.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import com.scd.gitlabtimeback.dto.UserDetailsDto;
import com.scd.gitlabtimeback.entity.User;
import com.scd.gitlabtimeback.service.UserService;

import java.util.Map;

@Component
public class UserApi {

    private final UserService userService;

    public UserApi(@Autowired UserService userService) {
        this.userService = userService;
    }

    public UserDetailsDto getUserById(Long userId) {
        if (userId == null) {
            throw new NullPointerException();
        }

        User user = userService.findById(userId);
        if (user == null) {
            throw new NullPointerException();
        }

        return new UserDetailsDto(
                true,
                userId,
                user.getName(),
                user.getAdmin());
    }

    public UserDetailsDto getCurrentUser() {

        Authentication authentication = ((OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication())
                .getUserAuthentication();

        @SuppressWarnings("unchecked")
        Map<String, Object> m = (Map<String, Object>) authentication.getDetails();

        Long userId = Long.valueOf((Integer) m.get("id"));

        return getUserById(userId);
    }

}
