package com.prs.rs.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PlatformInfoDto {

    private Long platformId;
    private String name;
    private String url;
    private String description;
    private Integer score;

}
