package com.scd.gitlabtimeback.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scd.gitlabtimeback.api.UserApi;
import com.scd.gitlabtimeback.dto.UserDetailsDto;


@RestController
@RequestMapping(path = "/api")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {

    private final UserApi userApi;

    public UserController(@Autowired UserApi userApi) {
        this.userApi = userApi;
    }


    @GetMapping(path = "user")
    public UserDetailsDto getUser() {

        return userApi.getCurrentUser();
    }


}
