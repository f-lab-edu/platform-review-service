package com.prs.ms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.prs.ms.domain.Member;
import com.prs.ms.dto.MemberRegisterDto;
import com.prs.ms.exception.MemberSignUpException;
import com.prs.ms.repository.MemberRepository;
import com.prs.ms.type.MemberRole;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    private static final String username = "testUser";

    private static final String password = "as123as123";


    @Test
    @DisplayName("회원가입")
    void memberRegister() {

        // given
        MemberRegisterDto request = new MemberRegisterDto(username, password);

        String encryptPassword = passwordEncoder.encode(password);
        when(memberRepository.save(any())).thenReturn(
            new Member(request.getUsername(), encryptPassword, MemberRole.ROLE_USER));

        // when
        Member member = memberService.register(request);

        // then
        assertEquals(request.getUsername(), member.getUsername());
        assertEquals(encryptPassword, member.getPassword());
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