package com.prs.ps.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    @Operation(hidden = true)
    public String home() {
        return "Platform-Service is Running";
    }

}
