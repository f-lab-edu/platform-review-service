package com.prs.ps.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlatformLeastInfoDto {

    private Long platformId;
    private String name;
    private String url;
    private String description;
    private Byte star;
}
