package com.prs.ps.common;

import com.prs.ps.domain.Platform;

public class CommonUtils {

    public static Platform getMockPlatform(String platformName) {
        Platform mockPlatform = new Platform(platformName, "https://naver.com",
            "검색엔진 플랫폼입니다.", 1L);
        return mockPlatform;
    }
}
