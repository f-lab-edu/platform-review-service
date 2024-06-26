package com.prs.rs.service;


import com.library.common.dto.MemberInfoDto;
import com.prs.rs.domain.Review;
import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.dto.response.ReviewListResultDto;

public interface ReviewService {

    Review addReview(Long platformId, PlatformInfoDto platformInfoDto, MemberInfoDto memberInfoDto,
        ReviewWriteDto reviewWriteDto);

    Review updateReview(Long reviewId, Review review, MemberInfoDto memberInfoDto,
        ReviewEditDto reviewEditDto);

    Boolean deleteReview(Long reviewId, Review review, MemberInfoDto memberInfoDto);

    ReviewListResultDto getReviewList(ReviewListDto reviewListDto, Long platformId,
        PlatformInfoDto mockObject);

}
