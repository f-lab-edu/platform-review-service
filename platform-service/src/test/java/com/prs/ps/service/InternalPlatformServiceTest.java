package com.prs.ps.service;


import static com.prs.ps.common.CommonUtils.getMockPlatform;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformLeastInfoDto;
import com.prs.ps.exception.PlatformAccessDeniedException;
import com.prs.ps.exception.PlatformNotFoundException;
import com.prs.ps.repository.PlatformRepository;
import com.prs.ps.type.PlatformStatus;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InternalPlatformServiceTest {

    @InjectMocks
    InternalPlatformServiceImpl platformService;

    @Mock
    PlatformRepository platformRepository;


    @Test
    @DisplayName("findPlatformInfoByIdTest()")
    void findPlatformInfoByIdTest() {
        // given
        Platform savedPlatform = spy(getMockPlatform("네이버"));
        savedPlatform.changeInfo(savedPlatform.getDescription(), PlatformStatus.ACCEPT);

        when(savedPlatform.getId()).thenReturn(1L);

        //when
        PlatformLeastInfoDto platformInfo = platformService.findPlatformInfoById(
            savedPlatform.getId(),
            savedPlatform);

        // then
        Assertions.assertThat(platformInfo.getPlatformId()).isEqualTo(savedPlatform.getId()); // 아이디
        Assertions.assertThat(platformInfo.getName()).isEqualTo(savedPlatform.getName()); // 이름
        Assertions.assertThat(platformInfo.getUrl()).isEqualTo(savedPlatform.getUrl()); // URL
        Assertions.assertThat(platformInfo.getScore()).isEqualTo(savedPlatform.getAvgScore()); // 평점
        Assertions.assertThat(platformInfo.getDescription())
            .isEqualTo(savedPlatform.getDescription()); // 설명
    }


    @Test
    @DisplayName("findPlatformInfoByIdTest() - 허용되지 않은 플랫폼에 대한 접근")
    void findPlatformInfoByIdEx1() {
        // given
        Platform savedPlatform = spy(getMockPlatform("네이버"));

        when(savedPlatform.getId()).thenReturn(1L);

        // when & then
        Assertions.assertThatThrownBy(() -> platformService.findPlatformInfoById(
            savedPlatform.getId(),
            savedPlatform)).isInstanceOf(PlatformAccessDeniedException.class);
    }


}
