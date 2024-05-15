package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.request.MemberRegisterDto;
import com.review.rsproject.exception.MemberSignUpException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.type.MemberRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
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

    private static final String username = "testuser";
    String password;

    @BeforeEach
    void before() {
        password = "123456789a";
    }

    @Test
    @DisplayName("회원가입")
    void memberRegister() {

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
    void memberRegisterDuplicate() {
        // given
        MemberRegisterDto request = new MemberRegisterDto(username, password);

        Member member = new Member(request.getUsername(), password, MemberRole.ROLE_USER);
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));

        // then
        assertThrows(MemberSignUpException.class, () -> memberService.register(request));


    }
}