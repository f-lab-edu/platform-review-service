package com.review.rsproject.service;

import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.PlatformApplyDto;
import com.review.rsproject.dto.PlatformEditDto;
import com.review.rsproject.dto.PlatformPageDto;
import com.review.rsproject.type.PlatformStatus;

public interface PlatformService {

        Platform addPlatform(PlatformApplyDto applyDto);

        Platform updatePlatform(PlatformEditDto editDto);

        PlatformPageDto getPlatformList(Integer page, PlatformStatus status);

}
