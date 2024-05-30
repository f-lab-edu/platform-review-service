package com.review.rsproject.integration;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.MemberRegisterDto;
import com.review.rsproject.dto.request.PlatformApplyDto;
import com.review.rsproject.dto.request.PlatformEditDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.service.MemberService;
import com.review.rsproject.service.PlatformService;
import com.review.rsproject.service.ReviewService;
import com.review.rsproject.type.PlatformStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.review.rsproject.common.CommonUtils.setContextByUsername;

@SpringBootTest
public class ReviewIntegrationTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    MemberService memberService;

    @Autowired
    PlatformService platformService;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("리뷰 삭제")
    @Transactional
    void reviewDelete() {
        // given
            // 회원가입
        Member member = memberService.register(new MemberRegisterDto("test13131313", "asdfg123123"));
        setContextByUsername(member.getUsername());
            // 플랫폼 등록
        Platform platform = platformService.addPlatform(new PlatformApplyDto("naver", "https://naver.com", "한국의 검색엔진 플랫폼"));
        platformService.updatePlatform(new PlatformEditDto(platform.getId(), platform.getDescription(), PlatformStatus.ACCEPT));

            // 리뷰 작성
        Review writedReview = reviewService.addReview(new ReviewWriteDto(platform.getId(), "asdsdfafdsafsxvc", (byte) 5));

        // when
        reviewService.deleteReview(writedReview.getId());


        // then
        entityManager.flush();
        entityManager.clear();

        Optional<Platform> findPlatform = platformRepository.findById(writedReview.getPlatform().getId());
        if (findPlatform.isPresent()) {
            Assertions.assertThat(findPlatform.get().getStar()).isEqualTo((byte) 0);
            Assertions.assertThat(findPlatform.get().getReviews().size()).isEqualTo( 0);
        }

    }
}
