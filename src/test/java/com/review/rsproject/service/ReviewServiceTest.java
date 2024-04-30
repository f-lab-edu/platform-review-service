package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.repository.ReviewRepository;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    void review_write() {
        // given
        Member member = new Member(USERNAME, "123123", MemberRole.ROLE_USER);
        setContextByUsername(USERNAME);

        Platform platform = new Platform("네이버", "https://naver.com", "네이버버법", member);

            // 검증 Mock
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        when(memberRepository.findByUsername(USERNAME)).thenReturn(Optional.of(member));

            // 플랫폼 평점 업데이트 Mock
        List<Long[]> reviewData = new ArrayList<>();
        reviewData.add(new Long[] {1L, 5L}); // 1개의 리뷰, 총 리뷰 점수 합계 5점)
        when(reviewRepository.findByStar(any())).thenReturn(reviewData);


        // when
        Review review = reviewService.addReview(new ReviewWriteDto(1L, "아아아", (byte) 5));

        // then
        Assertions.assertThat(review.getPlatform().getReviews().size()).isEqualTo(1);


    }

    @Test
    @DisplayName("리뷰 수정")
    void review_edit() {
        // given
        setContextByUsername(USERNAME);
        Member member = new Member(USERNAME, "1111", MemberRole.ROLE_USER);
        Platform platform = new Platform("네이버", "https://naver.com", "네이버버버버", member);
        platform.changeInfo(null, PlatformStatus.ACCEPT);
        Review review = new Review(platform, member, "수정되기 전의 리뷰", (byte) 5);


        when(reviewRepository.findByIdFetchOther(any())).thenReturn(Optional.of(review));

            // 플랫폼 평점 업데이트 Mock
        List<Long[]> reviewData = new ArrayList<>();
        reviewData.add(new Long[] {1L, 10L}); // 1개의 리뷰, 총 리뷰 점수 합계 10점)
        when(reviewRepository.findByStar(any())).thenReturn(reviewData);


        // when
        Review result = reviewService.updateReview(new ReviewEditDto(1L, "수정된 리뷰", (byte) 10));



        // then
        Assertions.assertThat(result.getStar()).isEqualTo((byte)10);
        Assertions.assertThat(result.getContent()).isEqualTo("수정된 리뷰");
    }


    private void setContextByUsername(String username) {
        UserDetails userDetails = new User(username, "123123", new ArrayList<>());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

}