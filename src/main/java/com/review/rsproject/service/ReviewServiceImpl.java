package com.review.rsproject.service;

import com.review.rsproject.annotation.Retry;
import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewListDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.dto.response.ReviewListResultDto;

import com.review.rsproject.repository.ReviewRepository;

import com.review.rsproject.type.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;

import static com.review.rsproject.common.ConstantValues.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {


    private final ReviewPersistenceManager reviewPersistenceManager;
    private final ReviewRepository reviewRepository;
    private final CacheControlManager cacheControlManager;




    @Override
    @Retry
    public Review addReview(ReviewWriteDto reviewWriteDto) {
        return reviewPersistenceManager.validateAndWriteReview(reviewWriteDto);
    }



    @Override
    @Retry
    public Review updateReview(ReviewEditDto reviewEditDto) {
            Review review = reviewPersistenceManager.validateAndUpdateReview(reviewEditDto);
            removeCache(review.getPlatform().getId());
            return review;
    }

    @Override
    @Retry
    public void deleteReview(Long reviewId) {
            Long platformId = reviewPersistenceManager.validateAndDeleteReview(reviewId);
            removeCache(platformId);
    }


    @Override
    @Cacheable(value = "reviews", key = "#reviewListDto.id + '#' + #reviewListDto.page + '#' + #reviewListDto.sort", cacheManager = "redisCacheManager")
    public ReviewListResultDto getReviewList(ReviewListDto reviewListDto) {

        Platform platform = reviewPersistenceManager.validatePlatform(reviewListDto.getId());

        Pageable pageRequest = PageRequest.of(reviewListDto.getPage(), PAGE_SIZE, sortConverter(reviewListDto.getSort()));
        Page<Review> reviews = reviewRepository.findByIdFromPlatform(platform.getId(), pageRequest);


        return createReviewResultDto(platform, reviews);
    }

    private ReviewListResultDto createReviewResultDto(Platform platform, Page<Review> reviews) {
        ReviewListResultDto result = ReviewListResultDto.builder()
                .platformNo(platform.getId())
                .platformName(platform.getName())
                .platformUrl(platform.getUrl())
                .platformDescription(platform.getDescription())
                .platformStar(platform.getStar())
                .totalReview(reviews.getTotalElements())
                .pageNo(reviews.getNumber())
                .reviewList(new ArrayList<>())
                .totalPage(reviews.getTotalPages()).build();

        for (Review review : reviews.getContent()) {
            ReviewListResultDto.Dto dto = ReviewListResultDto.Dto.builder()
                    .reviewNumber(review.getId())
                    .memberName(review.getMember().getUsername())
                    .content(review.getContent())
                    .star(review.getStar())
                    .createdDt(review.getCreatedDt())
                    .modifiedDt(review.getModifiedDt()).build();
            result.getReviewList().add(dto);
        }
        return result;
    }


    /*
    * 플랫폼 ID에 해당하는 리뷰 캐시 삭제
     */
    private void removeCache(Long platformId) {
        String pattern = REVIEW_CACHE + "::" + platformId;
        cacheControlManager.evictCacheByPattern(pattern);
    }




    /*
    * SortType -> Sort
    * */
    private Sort sortConverter(SortType sort) {
        switch (sort) {
            case STAR_ASC ->  { return Sort.by(Sort.Direction.ASC, "star"); }
            case STAR_DESC ->  { return Sort.by(Sort.Direction.DESC, "star"); }
            case DATE_DESC -> { return Sort.by(Sort.Direction.DESC, "createdDt"); }
            default ->  { return Sort.by(Sort.Direction.ASC, "createdDt"); }
        }
    }




}
