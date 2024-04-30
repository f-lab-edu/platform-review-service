package com.review.rsproject.controller;


import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.dto.request.PlatformApplyDto;
import com.review.rsproject.dto.request.PlatformEditDto;
import com.review.rsproject.dto.request.SearchDto;
import com.review.rsproject.dto.response.PlatformInfoDto;
import com.review.rsproject.dto.response.PlatformPageDto;
import com.review.rsproject.dto.response.PlatformSearchDto;
import com.review.rsproject.service.PlatformService;
import com.review.rsproject.type.PlatformSort;
import com.review.rsproject.type.PlatformStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/platform/search")
    public PlatformSearchDto searchPlatform(@ModelAttribute @Valid SearchDto searchDto) {
        return platformService.getPlatformSearchResult(searchDto);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/platform")
    public String editPlatform(@RequestBody @Valid PlatformEditDto editDto) {
        platformService.updatePlatform(editDto);
        return "ok";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/platforms")
    public PlatformPageDto listPlatform(@RequestParam(name = "page") Integer page, @RequestParam(name = "status") @Nullable PlatformStatus status) {
        return platformService.getPlatformList(page, status);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/platform/{id}")
    public PlatformInfoDto infoPlatform(@PathVariable(name = "id") Long id) {
        return platformService.getPlatformInfo(id);
    }



}
