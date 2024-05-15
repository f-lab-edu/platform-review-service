package com.review.rsproject.repository;

import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.response.ReviewCountDto;
import com.review.rsproject.type.MemberRole;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;


@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 메모리 DB 사용 X
class ReviewRepositoryTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired PlatformRepository platformRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("findByStar() - 플랫폼 리뷰 수랑 리뷰별 합계가 잘 반환되는지")
    void findStar() {
        // given
        List<Review> reviews = buildMockReview();

        // when
        ReviewCountDto result = reviewRepository.findByStar(reviews.get(0).getPlatform().getId());

        // then
        Assertions.assertThat(result.getReviewCount()).isEqualTo(2L); // 리뷰 수
        Assertions.assertThat(result.getReviewTotalStar()).isEqualTo(12L); // 별점 합계

    }

    @Test
    @DisplayName("findByPlatform() - 플랫폼에 따른 리뷰들이 잘 반환되는지")
    void findPlatformReview() {
        // given
        List<Review> reviews = buildMockReview();

        Long platformId = reviews.get(0).getPlatform().getId();
        PageRequest pageRequest = PageRequest.of(0, ConstantValues.PAGE_SIZE);

        // when
        Page<Review> platforms = reviewRepository.findByIdFromPlatform(platformId, pageRequest);

        // then
        Assertions.assertThat(platforms.getTotalElements()).isEqualTo(2);

    }


    private List<Review> buildMockReview() {
        Member member = new Member("test", "1111", MemberRole.ROLE_USER);
        memberRepository.save(member);

        Platform platform = new Platform("네이버", "https://naver.com", "네이버버버", member);
        platformRepository.save(platform);

        Review review1 = new Review(platform, member, "asdfg", (byte) 5);
        Review review2 = new Review(platform, member, "asdfg", (byte) 7);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

       List<Review> reviews = new ArrayList<>();
       reviews.add(review1);
       reviews.add(review2);

        return reviews;
    }

}