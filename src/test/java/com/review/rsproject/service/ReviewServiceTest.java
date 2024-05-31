package com.review.rsproject.service;

import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewListDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.dto.response.ReviewCountDto;
import com.review.rsproject.dto.response.ReviewListResultDto;
import com.review.rsproject.exception.ReviewAccessDeniedException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.repository.ReviewRepository;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;
import com.review.rsproject.type.SortType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.review.rsproject.common.CommonUtils.mockBuildPlatform;
import static com.review.rsproject.common.CommonUtils.setContextByUsername;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {





    @Mock
    private ReviewPersistenceManager reviewPersistenceManager;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;



    @Test
    @DisplayName("리뷰 조회")
    void reviewList() {
        // given
        Long reviewCount = 15L;

        Platform platform = mockBuildPlatform();
        List<Review> reviews = mockBuildBulkReview(platform, reviewCount);

        ReviewListDto requestDto = new ReviewListDto(1L, 0, SortType.DATE_DESC);

            // mock
        when(reviewPersistenceManager.validatePlatform(any())).thenReturn(platform);
        when(reviewRepository.findByIdFromPlatform(any(), any()))
                .thenReturn(new PageImpl<>(reviews, PageRequest.of(0, ConstantValues.PAGE_SIZE), reviewCount));




        // when
        ReviewListResultDto result = reviewService.getReviewList(requestDto);


        // then
        Assertions.assertThat(result.getPlatformName()).isEqualTo(platform.getName()); // 플랫폼 이름 검증
        Assertions.assertThat(result.getTotalReview()).isEqualTo(reviewCount); // 총 리뷰 수 검증
        Assertions.assertThat(result.getTotalPage()).isEqualTo(2); // 총 페이지 수 검증


    }



    private List<Review> mockBuildBulkReview(Platform platform, Long count) {
        List<Review> reviews = new ArrayList<>();
        for(int i = 1; i <= count; ++i) {
            reviews.add(new Review(platform, platform.getMember(), "평범한 리뷰입니다.", (byte) 5));
        }
        return reviews;
    }
}