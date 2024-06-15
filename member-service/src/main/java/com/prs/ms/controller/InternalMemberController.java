package com.prs.ms.controller;


import com.prs.ms.dto.MemberResponseDto;
import com.prs.ms.service.InternalMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "회원 내부 호출 API", description = "다른 서비스 호출을 위한 API")
public class InternalMemberController {

    private final InternalMemberService internalMemberService;


    /*
    * 요청하는 회원 정보를 반환
    * */
    @GetMapping("/member")
    public MemberResponseDto memberInfo() {
        return internalMemberService.getMemberInfo();
    }




    @GetMapping("/auth/check-admin")
    public Boolean checkAdmin() {
        return internalMemberService.checkAdmin();
    }
}
