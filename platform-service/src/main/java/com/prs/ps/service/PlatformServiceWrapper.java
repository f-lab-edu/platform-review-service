package com.prs.ps.service;

import com.prs.ps.annotation.Retry;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformRefreshDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlatformServiceWrapper {

    private final PlatformService platformService;

    @Retry
    public void refreshPlatformScore(Long platformId, Platform emptyPlatform,
        PlatformRefreshDto platformRefreshDto) {
        platformService.refreshPlatformScore(platformId,
            emptyPlatform, platformRefreshDto);
    }


}
