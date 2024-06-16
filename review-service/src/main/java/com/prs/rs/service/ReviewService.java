package com.prs.rs.service;


import com.prs.rs.domain.Review;
import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.dto.response.ReviewListResultDto;

public interface ReviewService {

    Review addReview(ReviewWriteDto reviewWriteDto);

    Review updateReview(ReviewEditDto reviewEditDto);

    void deleteReview(Long id);

    ReviewListResultDto getReviewList(ReviewListDto reviewListDto, Long platformId, PlatformInfoDto mockObject);

}
