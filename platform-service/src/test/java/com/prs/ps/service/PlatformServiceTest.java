package com.prs.ps.service;

import com.library.common.client.MemberServiceClient;
import com.library.common.dto.MemberInfoDto;
import com.prs.ps.common.ConstantValues;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.request.PlatformApplyDto;
import com.prs.ps.dto.request.PlatformEditDto;
import com.prs.ps.dto.request.PlatformSearchDto;
import com.prs.ps.dto.response.PlatformInfoDto;
import com.prs.ps.dto.response.PlatformSearchResultDto;
import com.prs.ps.repository.PlatformRepository;
import com.prs.ps.type.PlatformStatus;
import com.prs.ps.type.SortType;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import static com.prs.ps.common.CommonUtils.getMockPlatform;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlatformServiceTest {

    @Mock
    PlatformRepository platformRepository;

    @InjectMocks
    PlatformServiceImpl platformService;

    @Mock
    MemberServiceClient memberServiceClient;


    private static final String DEFAULT_USERNAME = "testUser";
    private static final String DEFAULT_PLATFORM_NAME = "네이버";

    @Test
    @DisplayName("addPlatformTest() - 플랫폼 등록")
    void addPlatformTest() {

        // given
        PlatformApplyDto requestDto = new PlatformApplyDto(DEFAULT_PLATFORM_NAME,
            "https://naver.com",
            "검색엔진 플랫폼");

        // 멤버 정보 생성
        MemberInfoDto memberInfoDto = new MemberInfoDto();
        memberInfoDto.setMemberId(1L);
        memberInfoDto.setName(DEFAULT_USERNAME);

        // 플랫폼 정보 생성
        Platform mockPlatform = getMockPlatform("네이버");

        when(platformRepository.save(any())).thenReturn(mockPlatform);

        // when
        Platform platform = platformService.addPlatform(requestDto, memberInfoDto);

        // then
        Assertions.assertThat(platform).isSameAs(mockPlatform);

    }


    @Test
    @DisplayName("updatePlatformTest() - 플랫폼 수정 테스트")
    void updatePlatformTest() {
        // given
        Platform savedPlatform = getMockPlatform("네이버");

        PlatformEditDto requestDto = new PlatformEditDto(savedPlatform.getId(), "수정된 플랫폼입니다.",
            PlatformStatus.ACCEPT);

        // when
        Platform updatedPlatform = platformService.updatePlatform(requestDto,
            requestDto.getPlatformId(),
            savedPlatform);

        // then
        Assertions.assertThat(updatedPlatform.getDescription())
            .isEqualTo(requestDto.getDescription());

        Assertions.assertThat(updatedPlatform.getStatus()).isEqualTo(requestDto.getStatus());
    }


    @Test
    @DisplayName("getPlatformSearchResultTest() - 플랫폼 검색 테스트")
    void getPlatformSearchResultTest() {
        // given
        int totalSize = 13; // 총 플랫폼 수가 13개라는 가정 하에 테스트

        List<Platform> platformList = new ArrayList<>();
        for (int i = 1; i <= 10; ++i) {
            platformList.add(getMockPlatform(DEFAULT_PLATFORM_NAME + i));
        }

        // Page 객체 생성
        Pageable pageable = PageRequest.of(0, ConstantValues.PAGE_SIZE);
        Page<Platform> platformPage = new PageImpl<>(platformList, pageable, totalSize);

        // Stub
        when(platformRepository.findByQuery(DEFAULT_PLATFORM_NAME, pageable,
            SortType.DATE_ASC)).thenReturn(
            platformPage);

        // request 객체 생성
        PlatformSearchDto requestDto = new PlatformSearchDto(DEFAULT_PLATFORM_NAME, 0,
            SortType.DATE_ASC);

        // when
        PlatformSearchResultDto result = platformService.getPlatformSearchResult(
            requestDto);

        // then
        Assertions.assertThat(result.getNowPage()).isEqualTo(0);
        Assertions.assertThat(result.getPlatformCount()).isEqualTo(10); // 현재 페이지의 데이터 갯수
        Assertions.assertThat(result.getTotalSize()).isEqualTo(totalSize);

    }


    @Test
    @DisplayName("getPlatformInfoTest() - 특정 플랫폼 조회 테스트")
    void getPlatformInfoTest() {
        // given
        Platform savedPlatform = spy(getMockPlatform("네이버"));

        when(savedPlatform.getId()).thenReturn(1L);

        MemberInfoDto memberInfo = new MemberInfoDto(1L, "testUser");

        when(memberServiceClient.getMemberInfoById(any())).thenReturn(memberInfo);

        // when
        PlatformInfoDto platformInfo = platformService.getPlatformInfo(savedPlatform.getId(),
            savedPlatform);

        // then
        Assertions.assertThat(platformInfo.getPlatformName())
            .isEqualTo(savedPlatform.getName()); // 이름

        Assertions.assertThat(platformInfo.getDescription())
            .isEqualTo(savedPlatform.getDescription()); // 설명

        Assertions.assertThat(platformInfo.getUrl()).isEqualTo(savedPlatform.getUrl()); // URL

        Assertions.assertThat(platformInfo.getMemberName())
            .isEqualTo(memberInfo.getName()); // 등록한 멤버 이름
    }


}