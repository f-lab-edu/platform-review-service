package com.review.rsproject.service;

import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewListDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.dto.response.ReviewListResultDto;

public interface ReviewService {

    Review addReview(ReviewWriteDto reviewWriteDto);

    Review updateReview(ReviewEditDto reviewEditDto);

    void deleteReview(Long id);

    ReviewListResultDto getReviewList(ReviewListDto reviewListDto);

}
