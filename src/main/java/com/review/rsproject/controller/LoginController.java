package com.review.rsproject.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/failed")
    public String fail() {
        return "failed";
    }

    @GetMapping("/success")
    public String success() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
