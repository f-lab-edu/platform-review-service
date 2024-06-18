package com.prs.ps.controller;


import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformLeastInfoDto;
import com.prs.ps.service.InternalPlatformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/in")
@RequiredArgsConstructor
public class InternalPlatformController {

    private final InternalPlatformService internalPlatformService;

    @GetMapping("/platforms/{platformId}")
    public PlatformLeastInfoDto platformInfo(@PathVariable("platformId") Long platformId) {
        return internalPlatformService.findPlatformInfoById(platformId, Platform.getEmpty());
    }

}
