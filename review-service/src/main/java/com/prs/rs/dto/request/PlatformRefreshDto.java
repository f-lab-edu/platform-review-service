package com.prs.rs.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlatformRefreshDto {


    private Long platformId;
    private Long reviewCount;
    private Long reviewTotalStar;


}
