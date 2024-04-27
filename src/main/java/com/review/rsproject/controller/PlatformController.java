package com.review.rsproject.controller;


import com.review.rsproject.dto.PlatformApplyDto;
import com.review.rsproject.service.PlatformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PlatformController {

    private final PlatformService platformService;


    @PostMapping("/platform")
    public String applyPlatform(@RequestBody PlatformApplyDto applyDto) {
        platformService.addPlatform(applyDto);
        return "ok";
    }

}
