package com.scd.gitlabtimeback.Role;

import com.scd.gitlabtimeback.service.UserService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

public class UserAdmin implements GrantedAuthority {

    private final UserService userService;
    private final Long userId;

    public UserAdmin(@Autowired UserService userService, @NotNull Long userId) {
        this.userService = userService;
        this.userId = userId;
    }

    @Override
    public String getAuthority() {

        return userService.userIsAdmin(userId) ? "ROLE_ADMIN" : "ROLE_USER";
    }
}