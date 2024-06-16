package com.prs.rs.service;


import com.prs.rs.aop.ValidatePlatform;
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

    private final ReviewRepository reviewRepository;




    @Override
    public Review addReview(ReviewWriteDto reviewWriteDto) {
        // 유효한 멤버인지 검증하는 로직이 들어가야 한다.

        // 플랫폼이 유효한 플랫폼인지 검증하는 로직이 들어가야 한다.

        Long memberId = 2L;
        Long platformId = 1L;

        // 리뷰 저장
        Review review = new Review(platformId, memberId, reviewWriteDto.getContent(), reviewWriteDto.getStar());
        reviewRepository.save(review);

        // 플랫폼 평점 업데이트 로직이 추가되어야 함.


        return review;
    }





    @Override
    public Review updateReview(ReviewEditDto reviewEditDto) {
            // Review review = reviewPersistenceManager.validateAndUpdateReview(reviewEditDto);
            // removeCache(review.getPlatform().getId());
            return null;
    }

    @Override
    public void deleteReview(Long reviewId) {
            // Long platformId = reviewPersistenceManager.validateAndDeleteReview(reviewId);
            // removeCache(platformId);
    }

    public PlatformInfoDto validatePlatform(Long platformId) {
//        Optional<Platform> platform = platformRepository.findById(platformId);
//
//        if (platform.isEmpty()) {
//            throw new PlatformNotFoundException();
//        }
//        if (platform.get().getStatus() != PlatformStatus.ACCEPT) {
//            throw new PlatformAccessDeniedException();
//        }


        return null;
    }


    @Override
    public ReviewListResultDto getReviewList(ReviewListDto reviewListDto, @ValidatePlatform Long platformId, PlatformInfoDto platform) {

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
