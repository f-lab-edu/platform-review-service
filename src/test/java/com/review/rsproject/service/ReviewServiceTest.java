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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlatformRepository platformRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private static final String USERNAME = "test_user";

    @Test
    @DisplayName("리뷰 작성")
    void reviewWrite() {
        // given
        setContextByUsername(USERNAME);

        Platform platform = mockBuildPlatform();

        // 검증 Mock
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        when(memberRepository.findByUsername(USERNAME)).thenReturn(Optional.of(platform.getMember()));

            // 플랫폼 평점 업데이트 Mock
        when(reviewRepository.findByStar(any())).thenReturn(new ReviewCountDto(1L, 5L));


        // when
        Review review = reviewService.addReview(new ReviewWriteDto(1L, "아아아", (byte) 5));

        // then
        Assertions.assertThat(review.getPlatform().getReviews().size()).isEqualTo(1);


    }

    @Test
    @DisplayName("리뷰 수정")
    void reviewEdit() {
        // given
        setContextByUsername(USERNAME);

        Review review = mockBuildReview();


        when(reviewRepository.findByIdFetchOther(any())).thenReturn(Optional.of(review));

            // 플랫폼 평점 업데이트 Mock, 1개의 리뷰 총점 10점
        when(reviewRepository.findByStar(any())).thenReturn(new ReviewCountDto(1L, 10L));


        // when
        Review result = reviewService.updateReview(new ReviewEditDto(1L, "수정된 리뷰", (byte) 10));



        // then
        Assertions.assertThat(result.getStar()).isEqualTo((byte)10);
        Assertions.assertThat(result.getContent()).isEqualTo("수정된 리뷰");
    }

    @Test
    @DisplayName("리뷰 수정, 다른 사람이 수정하려 하는 경우")
    void reviewEditOther() {
        // given
        setContextByUsername("bad_test_user");

        Review review = mockBuildReview();

        when(reviewRepository.findByIdFetchOther(any())).thenReturn(Optional.of(review));


        // when then
        assertThrows(ReviewAccessDeniedException.class, () -> reviewService.updateReview(new ReviewEditDto(1L, "수정된 리뷰", (byte) 10)));
    }

    @Test
    @DisplayName("리뷰 삭제, 다른 사람이 삭제하려는 경우")
    void reviewDelete() {
        // given
        setContextByUsername("bad_test_user");
        Review review = mockBuildReview();
        when(reviewRepository.findByIdFetchOther(any())).thenReturn(Optional.of(review));

        // then
        assertThrows(ReviewAccessDeniedException.class, () -> reviewService.deleteReview(1L));

    }

    @Test
    @DisplayName("리뷰 조회")
    void reviewList() {
        // given
        Long reviewCount = 15L;

        Platform platform = mockBuildPlatform();
        List<Review> reviews = mockBuildBulkReview(platform, reviewCount);

        ReviewListDto requestDto = new ReviewListDto(1L, 0, SortType.DATE_DESC);

            // mock
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        when(reviewRepository.findByIdFromPlatform(any(), any()))
                .thenReturn(new PageImpl<>(reviews, PageRequest.of(0, ConstantValues.PAGE_SIZE), reviewCount));


        // when
        ReviewListResultDto result = reviewService.getReviewList(requestDto);


        // then
        Assertions.assertThat(result.getPlatformName()).isEqualTo(platform.getName()); // 플랫폼 이름 검증
        Assertions.assertThat(result.getTotalReview()).isEqualTo(reviewCount); // 총 리뷰 수 검증
        Assertions.assertThat(result.getTotalPage()).isEqualTo(2); // 총 페이지 수 검증



    }


    private void setContextByUsername(String username) {
        UserDetails userDetails = new User(username, "123123", new ArrayList<>());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }


    private Review mockBuildReview() {
        Platform platform = mockBuildPlatform();
        return new Review(platform, platform.getMember(), "평범한 리뷰입니다.", (byte) 5);
    }

    private Platform mockBuildPlatform() {
        Member member = new Member(USERNAME, "1111", MemberRole.ROLE_USER);
        Platform platform =new Platform("네이버", "https://naver.com", "네이버버버버", member);
        return platform.changeInfo(platform.getDescription(), PlatformStatus.ACCEPT);
    }


    private List<Review> mockBuildBulkReview(Platform platform, Long count) {
        List<Review> reviews = new ArrayList<>();
        for(int i = 1; i <= count; ++i) {
            reviews.add(new Review(platform, platform.getMember(), "평범한 리뷰입니다.", (byte) 5));
        }
        return reviews;
    }
}