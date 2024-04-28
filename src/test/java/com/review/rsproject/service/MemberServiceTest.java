package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.MemberRegisterDto;
import com.review.rsproject.exception.MemberSignUpException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.type.MemberRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    String username;
    String password;

    @BeforeEach
    void before() {
        username = "testuser";
        password = "123456789a";
    }

    @Test
    @DisplayName("회원가입")
    void member_register() {

        // given
        String password = "123456789a";
        MemberRegisterDto request = new MemberRegisterDto(username, password);

        password = passwordEncoder.encode(password);
        when(memberRepository.save(any())).thenReturn(new Member(request.getUsername(), password, MemberRole.ROLE_USER));

        // when
        Member member = memberService.register(request);

        // then
        assertEquals(request.getUsername(), member.getUsername());
        assertEquals(password, member.getPassword());
        assertEquals(MemberRole.ROLE_USER, member.getRole());



    }

    @Test
    @DisplayName("회원가입 중복체크")
    void member_register_duplicate() {
        // given
        MemberRegisterDto request = new MemberRegisterDto(username, password);

        Member member = new Member(request.getUsername(), password, MemberRole.ROLE_USER);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));

        // then
        assertThrows(MemberSignUpException.class, () -> memberService.register(request));


    }
}