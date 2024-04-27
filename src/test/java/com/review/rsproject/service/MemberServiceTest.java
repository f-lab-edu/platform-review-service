package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.MemberRegisterDto;
import com.review.rsproject.exception.MemberSignUpException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Test
    void 회원가입() {
        // given
        String username = "test_user";
        MemberRegisterDto memberRegisterDto = new MemberRegisterDto(username, "123123");

        // when
        Member member = memberService.register(memberRegisterDto);

        // then
        Assertions.assertEquals(username, member.getUsername());

    }

    @Test
    void 회원가입_중복체크() {
        // given
        String username = "test_user";
        MemberRegisterDto memberRegisterDto = new MemberRegisterDto(username, "123123");

        // when
        Member member = memberService.register(memberRegisterDto);

       // then
        Assertions.assertThrows(MemberSignUpException.class, () -> memberService.register(memberRegisterDto));
    }
}