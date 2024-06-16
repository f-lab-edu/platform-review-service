package com.prs.ms.controller;


import com.prs.ms.dto.MemberResponseDto;
import com.prs.ms.service.InternalMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/in")
@RequiredArgsConstructor
@Tag(name = "회원 내부 호출 API")
public class InternalMemberController {

    private final InternalMemberService internalMemberService;



    /*
    * 특정 멤버 정보 반환
    * */
    @GetMapping("/members/{memberId}")
    public MemberResponseDto memberInfo(@PathVariable("memberId") Long memberId) {
        return internalMemberService.findMemberInfoById(memberId);
    }

    /*
    * 요청하고 있는 멤버의 정보 반환
    * */
    @GetMapping("/member")
    public MemberResponseDto requestMemberInfo() {
        return internalMemberService.findMemberInfo();
    }




    @GetMapping("/auth/check-admin")
    public Boolean checkAdmin() {
        return internalMemberService.checkAdmin();
    }
}