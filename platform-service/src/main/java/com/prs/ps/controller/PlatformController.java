package com.prs.ps.controller;


import com.prs.ps.annotation.RequiresPermission;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.request.PlatformApplyDto;
import com.prs.ps.dto.request.PlatformEditDto;
import com.prs.ps.dto.request.PlatformSearchDto;
import com.prs.ps.dto.response.PlatformInfoDto;
import com.prs.ps.dto.response.PlatformPageDto;
import com.prs.ps.dto.response.PlatformSearchResultDto;
import com.prs.ps.service.PlatformService;
import com.prs.ps.type.PlatformStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "플랫폼 API", description = "플랫폼에 대한 조회, 생성 API")
public class PlatformController {

    private final PlatformService platformService;


    @PostMapping("/platform")
    @Operation(summary = "플랫폼 등록")
    public String applyPlatform(@RequestBody @Valid PlatformApplyDto applyDto) {
        platformService.addPlatform(applyDto);
        return "ok";
    }

    @GetMapping("/platform/search")
    @Operation(summary = "플랫폼 검색")
    public PlatformSearchResultDto searchPlatform(
        @ModelAttribute @Valid PlatformSearchDto platformSearchDto) {
        return platformService.getPlatformSearchResult(platformSearchDto);
    }


    @RequiresPermission
    @PatchMapping("/platform")
    @Operation(summary = "플랫폼 수정")
    public String editPlatform(@RequestBody @Valid PlatformEditDto editDto) {
        platformService.updatePlatform(editDto, editDto.getPlatformId(), Platform.getEmpty());
        return "ok";
    }


    @RequiresPermission
    @GetMapping("/platforms")
    @Operation(summary = "플랫폼 단순 목록 조회")
    public PlatformPageDto listPlatform(@RequestParam(name = "page") Integer page,
        @RequestParam(name = "status") @Nullable PlatformStatus status) {
        return platformService.getPlatformList(page, status);
    }

    @RequiresPermission
    @GetMapping("/platform/{id}")
    @Operation(summary = "플랫폼 상세 조회")
    public PlatformInfoDto infoPlatform(@PathVariable(name = "id") Long id) {
        return platformService.getPlatformInfo(id, Platform.getEmpty());
    }


}
