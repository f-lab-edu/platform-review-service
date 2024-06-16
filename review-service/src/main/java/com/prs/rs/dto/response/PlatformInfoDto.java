package com.prs.rs.dto.response;


import lombok.Data;

@Data
public class PlatformInfoDto {

    private Long id;
    private String name;
    private String url;
    private String description;
    private Byte star;

}
