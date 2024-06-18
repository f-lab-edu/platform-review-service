package com.prs.ps.dto.response;

import com.prs.ps.type.PlatformStatus;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
