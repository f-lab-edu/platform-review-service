package com.prs.ps.service;


import static com.prs.ps.common.CommonUtils.getMockPlatform;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.library.common.client.MemberServiceClient;
import com.library.common.dto.MemberInfoDto;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.request.PlatformEditDto;
import com.prs.ps.dto.response.PlatformInfoDto;
import com.prs.ps.exception.PlatformNotFoundException;
import com.prs.ps.repository.PlatformRepository;
import com.prs.ps.type.PlatformStatus;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PlatformServiceBootTest {

    @MockBean
    PlatformRepository platformRepository;

    @Autowired
    PlatformService platformService;

    @MockBean
    MemberServiceClient memberServiceClient;

    @Test
    @DisplayName("updatePlatformTest() - 플랫폼 수정 테스트")
    void updatePlatformTest() {
        // given
        Platform savedPlatform = getMockPlatform("네이버");

        PlatformEditDto requestDto = new PlatformEditDto(savedPlatform.getId(), "수정된 플랫폼입니다.",
            PlatformStatus.ACCEPT);

        when(platformRepository.findById(any())).thenReturn(Optional.of(savedPlatform));

        // when
        Platform updatedPlatform = platformService.updatePlatform(requestDto,
            requestDto.getPlatformId(),
            Platform.getEmpty());

        // then
        Assertions.assertThat(updatedPlatform.getDescription())
            .isEqualTo(requestDto.getDescription());

        Assertions.assertThat(updatedPlatform.getStatus()).isEqualTo(requestDto.getStatus());
    }


    @Test
    @DisplayName("getPlatformInfoTest() - 특정 플랫폼 조회 테스트")
    void getPlatformInfoTest() {
        // given
        Platform savedPlatform = spy(getMockPlatform("네이버"));

        when(savedPlatform.getId()).thenReturn(1L);
        when(platformRepository.findById(any())).thenReturn(Optional.of(savedPlatform));

        MemberInfoDto memberInfo = new MemberInfoDto();
        memberInfo.setMemberId(1L);
        memberInfo.setName("testUser");

        when(memberServiceClient.getMemberInfoById(any())).thenReturn(memberInfo);

        // when
        PlatformInfoDto platformInfo = platformService.getPlatformInfo(savedPlatform.getId(),
            Platform.getEmpty());

        // then
        Assertions.assertThat(platformInfo.getPlatformName())
            .isEqualTo(savedPlatform.getName()); // 이름

        Assertions.assertThat(platformInfo.getDescription())
            .isEqualTo(savedPlatform.getDescription()); // 설명

        Assertions.assertThat(platformInfo.getUrl()).isEqualTo(savedPlatform.getUrl()); // URL

        Assertions.assertThat(platformInfo.getMemberName())
            .isEqualTo(memberInfo.getName()); // 등록한 멤버 이름
    }

    @Test
    @DisplayName("getPlatformInfoEx() - 플랫폼이 존재하지 않는 경우")
    void platformInfoEx() {
        // then
        Assertions.assertThatThrownBy(
                () -> platformService.getPlatformInfo(30L, Platform.getEmpty()))
            .isInstanceOf(PlatformNotFoundException.class);
    }


}
