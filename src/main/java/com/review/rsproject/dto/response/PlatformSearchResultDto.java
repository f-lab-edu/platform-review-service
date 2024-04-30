package com.review.rsproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformSearchResultDto {

    private String query;
    private Integer totalPage;
    private Integer nowPage;
    private Long totalSize;
    private Integer platformCount;

    private List<dto> platformList;



    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class dto {
        private String name;

        private String description;

        private Byte star;

        private Integer reviewCount;
    }

}
