package com.study.boot1.controller;

import com.study.boot1.service.EmailAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    EmailAuthService emailAuthService;

    @GetMapping("/auth")
    public Object authGET(String token) {
        return emailAuthService.auth(token);
    }
}
