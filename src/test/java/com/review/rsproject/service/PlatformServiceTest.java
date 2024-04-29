package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.PlatformApplyDto;
import com.review.rsproject.dto.PlatformEditDto;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    PlatformRepository platformRepository;

    @InjectMocks
    PlatformServiceImpl platformService;

    private static final String username = "testuser";



    @Test
    @DisplayName("플랫폼 등록")
    void platform_register() {
        // given
        Member member = new Member(username, "1111", MemberRole.ROLE_USER);

        PlatformApplyDto request = new PlatformApplyDto("네이버", "https://naver.com", "검색 엔진 포털 사이트입니다.");
        Platform response = new Platform(request.getName(), request.getUrl(), request.getDescription(), member);
add
        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(member));
        when(platformRepository.save(any())).thenReturn(response);

        setContextByUsername(username); // 스프링 시큐리티 Context 값 설정

        // when
        Platform platform = platformService.addPlatform(request);

        // then
        assertEquals(request.getName(), platform.getName());
        assertEquals(request.getDescription(), platform.getDescription());
        assertEquals(request.getUrl(), platform.getUrl());
        assertEquals(member.getUsername(), platform.getMember().getUsername());

    }

    @Test
    @DisplayName("플랫폼 등록자의 유저 정보가 DB에 없는 경우")
    void platform_register_ex() {
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());
        setContextByUsername(username);

        assertThrows(UsernameNotFoundException.class, () -> platformService.addPlatform(null));

    }

    @Test
    @DisplayName("플랫폼 수정")
    void platform_edit() {

        // given
        PlatformEditDto editDto = new PlatformEditDto(1L, "검색 엔진 포털 사이트였는데 바뀌었습니다.", PlatformStatus.ACCEPT);

        Platform original = new Platform("네이버", "https://naver.com", "검색 엔진 포털 사이트입니다.", null);
        when(platformRepository.findById(1L)).thenReturn(Optional.of(original));

        // when
        Platform resultPlatform = platformService.updatePlatform(editDto);


        // then
        assertEquals(editDto.getDescription(), resultPlatform.getDescription());
        assertEquals(editDto.getStatus(), resultPlatform.getStatus());
    }



    private void setContextByUsername(String _username) {
        UserDetails userDetails = new User(_username, "1111", new ArrayList<>());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }
}