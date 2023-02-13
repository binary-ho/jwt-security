package com.binaryho.jwtsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @GetMapping("home")
    public String home() {
        return "<h1>home sweet home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>내가 만든 Token~~~~</h1>";
    }
}
