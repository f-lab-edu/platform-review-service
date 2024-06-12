package com.prs.rs.service;


import com.prs.rs.aop.Retry;
import com.prs.rs.domain.Review;
import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.dto.response.ReviewListResultDto;
import com.prs.rs.repository.ReviewRepository;
import com.prs.rs.type.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

import static com.prs.rs.common.ConstantValues.PAGE_SIZE;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {


    private final ReviewPersistenceManager reviewPersistenceManager;
    private final ReviewRepository reviewRepository;




    @Override
    @Retry
    public Review addReview(ReviewWriteDto reviewWriteDto) {
        return reviewPersistenceManager.validateAndWriteReview(reviewWriteDto);
    }



    @Override
    @Retry
    public Review updateReview(ReviewEditDto reviewEditDto) {
            Review review = reviewPersistenceManager.validateAndUpdateReview(reviewEditDto);
            // removeCache(review.getPlatform().getId());
            return review;
    }

    @Override
    @Retry
    public void deleteReview(Long reviewId) {
            Long platformId = reviewPersistenceManager.validateAndDeleteReview(reviewId);
            // removeCache(platformId);
    }


    @Override
    // @Cacheable(value = "reviews", key = "#reviewListDto.id + '#' + #reviewListDto.page + '#' + #reviewListDto.sort", cacheManager = "redisCacheManager")
    public ReviewListResultDto getReviewList(ReviewListDto reviewListDto) {

        PlatformInfoDto platform = reviewPersistenceManager.validatePlatform(reviewListDto.getPlatformId());

        Pageable pageRequest = PageRequest.of(reviewListDto.getPage(), PAGE_SIZE, sortConverter(reviewListDto.getSort()));
        Page<Review> reviews = reviewRepository.findByIdFromPlatform(platform.getId(), pageRequest);


        return createReviewResultDto(platform, reviews);
    }

    private ReviewListResultDto createReviewResultDto(PlatformInfoDto platform, Page<Review> reviews) {
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
                    .memberName("testuser") // 유저 이름 넣어줘야함.
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
    //    private void removeCache(Long platformId) {
    //        String pattern = REVIEW_CACHE + "::" + platformId;
    //        cacheControlManager.evictCacheByPattern(pattern);
    //    }




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
