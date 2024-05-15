package com.review.rsproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @GetMapping("/failed")
    @Operation(hidden = true)
    public String fail() {
        return "failed";
    }

    @GetMapping("/success")
    @Operation(hidden = true)
    public String success() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
