package com.review.rsproject.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewCountDto {

    private Long reviewCount;
    private Long reviewTotalStar;

}
