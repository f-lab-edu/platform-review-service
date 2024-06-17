package com.prs.ps.dto.response;

import lombok.Data;

@Data
public class PlatformRefreshDto {

    private Long platformId;
    private Long reviewCount;
    private Long reviewTotalStar;

}
