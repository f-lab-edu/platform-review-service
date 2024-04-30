package com.review.rsproject.service;

import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewWriteDto;

public interface ReviewService {

    Review addReview(ReviewWriteDto reviewWriteDto);

    Review updateReview(ReviewEditDto reviewEditDto);

}
