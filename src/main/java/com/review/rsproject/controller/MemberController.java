package com.review.rsproject.controller;


import com.review.rsproject.dto.request.MemberRegisterDto;
import com.review.rsproject.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public String signup(@RequestBody @Valid MemberRegisterDto memberRegisterDto) {

        memberService.register(memberRegisterDto);
        return memberRegisterDto.getUsername();

    }
}
