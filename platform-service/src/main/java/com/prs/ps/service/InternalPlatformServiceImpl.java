package com.prs.ps.service;

import com.prs.ps.annotation.ValidatePlatform;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformLeastInfoDto;
import com.prs.ps.exception.PlatformAccessDeniedException;
import com.prs.ps.type.PlatformStatus;
import org.springframework.stereotype.Service;


@Service
public class InternalPlatformServiceImpl implements InternalPlatformService{

    @Override
    public PlatformLeastInfoDto findPlatformInfoById(@ValidatePlatform Long platformId, Platform platform) {

        if (!(platform.getStatus() == PlatformStatus.ACCEPT)) {
            throw new PlatformAccessDeniedException();
        }

        return PlatformLeastInfoDto.builder()
                .platformId(platform.getId())
                .url(platform.getUrl())
                .name(platform.getName())
                .star(platform.getStar())
                .description(platform.getDescription()).build();
    }
}
