package com.prs.ps.service;


import com.prs.ps.domain.Platform;
import com.prs.ps.dto.response.PlatformLeastInfoDto;

public interface InternalPlatformService {

    PlatformLeastInfoDto findPlatformInfoById(Long platformId, Platform mockObject);
}
