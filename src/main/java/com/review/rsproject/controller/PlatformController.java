package com.review.rsproject.controller;


import com.review.rsproject.dto.PlatformApplyDto;
import com.review.rsproject.dto.PlatformEditDto;
import com.review.rsproject.service.PlatformService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PlatformController {

    private final PlatformService platformService;


    @PostMapping("/platform")
    public String applyPlatform(@RequestBody @Valid PlatformApplyDto applyDto) {
        platformService.addPlatform(applyDto);
        return "ok";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/platform")
    public String editPlatform(@RequestBody @Valid PlatformEditDto editDto) {
        platformService.updatePlatform(editDto);
        return "ok";
    }

}
