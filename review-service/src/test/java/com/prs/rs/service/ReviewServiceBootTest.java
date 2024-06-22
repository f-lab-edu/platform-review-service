package com.prs.rs.service;

import static org.mockito.Mockito.when;

import com.library.validate.client.MemberServiceClient;
import com.library.validate.dto.MemberInfoDto;
import com.prs.rs.domain.Review;
import com.prs.rs.event.KafkaProducer;
import com.prs.rs.exception.ReviewAccessDeniedException;
import com.prs.rs.repository.ReviewRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ReviewServiceBootTest {


    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @MockBean
    MemberServiceClient memberServiceClient;

    @MockBean
    KafkaProducer kafkaProducer;

    @Test
    @DisplayName("deleteReviewTest() - 리뷰 삭제 테스트")
    void deleteReviewTest() {
        // given
        Review writtenReview = new Review(1L, 1L, "작성된 리뷰", 5);
        reviewRepository.save(writtenReview);

        when(memberServiceClient.getMemberInfo()).thenReturn(
            new MemberInfoDto(writtenReview.getMemberId(), "testUser"));

        // when
        reviewService.deleteReview(writtenReview.getId(), Review.getEmpty(), new MemberInfoDto());

        // then
        Optional<Review> findReview = reviewRepository.findById(writtenReview.getId());
        Assertions.assertThat(findReview.isEmpty()).isEqualTo(true);
    }


    @Test
    @DisplayName("deleteReviewEx1() - 리뷰 삭제를 요청하는 사람이 작성자가 아닌 경우")
    void deleteReviewEx1() {
        // given
        Review writtenReview = new Review(1L, 1L, "작성된 리뷰", 5);
        reviewRepository.save(writtenReview);

        when(memberServiceClient.getMemberInfo()).thenReturn(
            new MemberInfoDto(writtenReview.getMemberId() + 1, "testUser"));

        // when & then
        Assertions
            .assertThatThrownBy(
                () -> reviewService.deleteReview(writtenReview.getId(), Review.getEmpty(),
                    new MemberInfoDto()))
            .isInstanceOf(ReviewAccessDeniedException.class);
    }


    @Test
    @DisplayName("deleteReviewAdminTest() - 리뷰 삭제를 요청하는 사람이 작성자가 아니지만 어드민 권한을 가졌을 경우")
    void deleteReviewAdminTest() {
        // given
        Review writtenReview = new Review(1L, 1L, "작성된 리뷰", 5);
        reviewRepository.save(writtenReview);

        when(memberServiceClient.getMemberInfo()).thenReturn(
            new MemberInfoDto(writtenReview.getMemberId() + 1, "testUser"));
        when(memberServiceClient.checkAdmin()).thenReturn(true);

        // when
        reviewService.deleteReview(writtenReview.getId(), Review.getEmpty(), new MemberInfoDto());

        // then
        Optional<Review> findReview = reviewRepository.findById(writtenReview.getId());
        Assertions.assertThat(findReview.isEmpty()).isEqualTo(true);
    }
}
