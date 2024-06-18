package com.prs.ps.service;

import com.prs.ps.domain.Platform;
import com.prs.ps.dto.request.PlatformApplyDto;
import com.prs.ps.dto.request.PlatformEditDto;
import com.prs.ps.dto.request.PlatformSearchDto;
import com.prs.ps.dto.response.PlatformInfoDto;
import com.prs.ps.dto.response.PlatformPageDto;
import com.prs.ps.dto.response.PlatformSearchResultDto;
import com.prs.ps.type.PlatformStatus;

public interface PlatformService {

    Platform addPlatform(PlatformApplyDto applyDto);

    Platform updatePlatform(PlatformEditDto editDto, Long platformId, Platform mockObject);

    PlatformPageDto getPlatformList(Integer page, PlatformStatus status);

    PlatformInfoDto getPlatformInfo(Long platformId, Platform mockObject);

    PlatformSearchResultDto getPlatformSearchResult(PlatformSearchDto platformSearchDto);

}
