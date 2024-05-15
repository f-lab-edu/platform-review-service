package com.review.rsproject.service;

import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.request.PlatformApplyDto;
import com.review.rsproject.dto.request.PlatformEditDto;
import com.review.rsproject.dto.request.PlatformSearchDto;
import com.review.rsproject.dto.response.PlatformInfoDto;
import com.review.rsproject.dto.response.PlatformSearchResultDto;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.SortType;
import com.review.rsproject.type.PlatformStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
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
    void platformRegister() {
        // given
        Member member = new Member(username, "1111", MemberRole.ROLE_USER);

        PlatformApplyDto request = new PlatformApplyDto("네이버", "https://naver.com", "검색 엔진 포털 사이트입니다.");
        Platform response = new Platform(request.getName(), request.getUrl(), request.getDescription(), member);

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
    @DisplayName("플랫폼 등록, 등록자의 유저 정보가 DB에 없는 경우")
    void platformRegisterEx() {
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());
        setContextByUsername(username);

        assertThrows(UsernameNotFoundException.class, () -> platformService.addPlatform(null));

    }

    private void setContextByUsername(String _username) {
        UserDetails userDetails = new User(_username, "1111", new ArrayList<>());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("플랫폼 수정")
    void platformEdit() {

        // given
        PlatformEditDto editDto = new PlatformEditDto(1L, "검색 엔진 포털 사이트였는데 바뀌었습니다.", PlatformStatus.ACCEPT);

        Platform platform = mockBuildPlatform(1).get(0);
        when(platformRepository.findById(1L)).thenReturn(Optional.of(platform));

        // when
        Platform resultPlatform = platformService.updatePlatform(editDto);


        // then
        assertEquals(editDto.getDescription(), resultPlatform.getDescription());
        assertEquals(editDto.getStatus(), resultPlatform.getStatus());
    }


    @Test
    @DisplayName("특정 플랫폼 조회")
    void platformInfoCheck() {
        // given
        Platform platform = mockBuildPlatform(1).get(0);
        when(platformRepository.findByIdAndFetchMember(1L)).thenReturn(Optional.of(platform));

        // when
        PlatformInfoDto platformInfo = platformService.getPlatformInfo(1L);

        // then
        Assertions.assertThat(platformInfo.getPlatformName()).isEqualTo(platform.getName());
        Assertions.assertThat(platformInfo.getUrl()).isEqualTo(platform.getUrl());
        Assertions.assertThat(platformInfo.getDescription()).isEqualTo(platform.getDescription());
        Assertions.assertThat(platformInfo.getStatus()).isEqualTo(platform.getStatus());
    }

    @Test
    @DisplayName("특정 플랫폼 조회, 플랫폼이 없는 경우")
    void platformInfoCheckEx() {
        // then
        assertThrows(PlatformNotFoundException.class, () -> platformService.getPlatformInfo(1L));
    }


    @Test
    @DisplayName("플랫폼 검색")
    void platformSearch() {
        // given

        int totalSize = 13; // 총 데이터 갯수가 13개 라는 가정 하에 테스트

        // Page 객체 생성
        Pageable pageable = PageRequest.of(0, ConstantValues.PAGE_SIZE);
        Page<Platform> platformPage = new PageImpl<>(mockBuildPlatform(10), pageable, totalSize);

        // Mock 설정
        when(platformRepository.findByQuery("네이버", pageable, SortType.DATE_DESC)).thenReturn(platformPage);


        // when
        PlatformSearchResultDto result = platformService.getPlatformSearchResult(new PlatformSearchDto("네이버", 0, SortType.DATE_DESC));


        // then
        Assertions.assertThat(result.getNowPage()).isEqualTo(0);
        Assertions.assertThat(result.getPlatformCount()).isEqualTo(10); // 현재 페이지의 데이터 갯수
        Assertions.assertThat(result.getTotalSize()).isEqualTo(totalSize);


    }


    private List<Platform> mockBuildPlatform(Integer size) {
        Member member = new Member(username, "1111", MemberRole.ROLE_USER);
        List<Platform> platforms = new ArrayList<>();
        for(int i = 1; i <= size; ++i) {
            platforms.add(new Platform("네이버" + i, "https://naver.com", "검색 엔진 포털 사이트입니다.", member));
        }
        return platforms;

    }




}