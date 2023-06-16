package com.scd.gitlabtimeback.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class authController {

    @GetMapping(path = "login")
    void login() {
    }

    @GetMapping(path = "logout")
    void logout() {
    }

}
