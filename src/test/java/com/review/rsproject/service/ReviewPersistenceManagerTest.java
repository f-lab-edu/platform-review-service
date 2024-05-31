package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;

import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.dto.response.ReviewCountDto;

import com.review.rsproject.exception.ReviewAccessDeniedException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.repository.ReviewRepository;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static com.review.rsproject.common.CommonUtils.mockBuildPlatform;
import static com.review.rsproject.common.CommonUtils.setContextByUsername;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewPersistenceManagerTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlatformRepository platformRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private EntityManager entityManager;


    @InjectMocks
    private ReviewPersistenceManager reviewPersistenceManager;

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
        Review review = reviewPersistenceManager.validateAndWriteReview(new ReviewWriteDto(1L, "아아아", (byte) 5));

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
        Review result = reviewPersistenceManager.validateAndUpdateReview(new ReviewEditDto(1L, "수정된 리뷰", (byte) 10));



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
        assertThrows(ReviewAccessDeniedException.class, () -> reviewPersistenceManager.validateAndUpdateReview(new ReviewEditDto(1L, "수정된 리뷰", (byte) 10)));
    }

    @Test
    @DisplayName("리뷰 삭제, 다른 사람이 삭제하려는 경우")
    void reviewDeleteOther() {
        // given
        setContextByUsername("bad_test_user");
        Review review = mockBuildReview();
        when(reviewRepository.findByIdFetchOther(any())).thenReturn(Optional.of(review));

        // then
        assertThrows(ReviewAccessDeniedException.class, () -> reviewPersistenceManager.validateAndDeleteReview(1L));

    }




    private Review mockBuildReview() {
        Platform platform = mockBuildPlatform();
        return new Review(platform, platform.getMember(), "평범한 리뷰입니다.", (byte) 5);
    }
}