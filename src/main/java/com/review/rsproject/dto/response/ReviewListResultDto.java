package com.review.rsproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReviewListResultDto {

    private Long platformNo;
    private String platformName;
    private String platformUrl;
    private String platformDescription;
    private Byte platformStar;

    private Integer pageNo;
    private Integer totalPage;
    private Long totalReview;

    List<Dto> reviewList;




    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Dto {
        private Long reviewNumber;
        private String memberName;
        private String content;
        private Byte star;
        private LocalDateTime createdDt;
        private LocalDateTime modifiedDt;
    }

}
