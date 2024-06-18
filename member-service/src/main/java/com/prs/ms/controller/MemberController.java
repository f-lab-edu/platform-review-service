package com.prs.ms.controller;

import com.prs.ms.dto.MemberRegisterDto;
import com.prs.ms.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "회원 API", description = "회원을 위한 API")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입")
    public String signup(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {
        memberService.register(memberRegisterDto);
        return memberRegisterDto.getUsername();
    }

}
