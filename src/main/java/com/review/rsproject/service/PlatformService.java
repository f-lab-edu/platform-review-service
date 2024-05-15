package com.review.rsproject.service;

import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.request.PlatformApplyDto;
import com.review.rsproject.dto.request.PlatformEditDto;
import com.review.rsproject.dto.request.PlatformSearchDto;
import com.review.rsproject.dto.response.PlatformInfoDto;
import com.review.rsproject.dto.response.PlatformPageDto;
import com.review.rsproject.dto.response.PlatformSearchResultDto;
import com.review.rsproject.type.PlatformStatus;

public interface PlatformService {

        Platform addPlatform(PlatformApplyDto applyDto);

        Platform updatePlatform(PlatformEditDto editDto);

        PlatformPageDto getPlatformList(Integer page, PlatformStatus status);

        PlatformInfoDto getPlatformInfo(Long id);

        PlatformSearchResultDto getPlatformSearchResult(PlatformSearchDto platformSearchDto);

}
