package com.review.rsproject.dto.response;


import com.review.rsproject.type.PlatformStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformPageDto {


    private Integer totalPage;

    private Integer nowPage;

    private Long totalSize;
    private Integer pageSize;

    private List<Dto> platformList = new ArrayList<>();



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dto {
        private Long id;

        private String name;

        private PlatformStatus status;
    }

}
