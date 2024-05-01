package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewEditDto;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.dto.response.ReviewCountDto;
import com.review.rsproject.exception.PlatformAccessDeniedException;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.exception.ReviewAccessDeniedException;
import com.review.rsproject.exception.ReviewNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.repository.ReviewRepository;
import com.review.rsproject.type.PlatformStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {


    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PlatformRepository platformRepository;

    @Override
    @Transactional
    public Review addReview(ReviewWriteDto reviewWriteDto) {
        Member member = validMember();
        Platform platform = validPlatform(reviewWriteDto.getId());


        // 리뷰 저장
        Review review = new Review(platform, member, reviewWriteDto.getContent(), reviewWriteDto.getStar());
        reviewRepository.save(review);

        // 플랫폼 평점 업데이트
        Platform updatedPlatform = refreshPlatformStar(platform);
        platformRepository.save(updatedPlatform);

        return review;
    }

    @Override
    @Transactional
    public Review updateReview(ReviewEditDto reviewEditDto) {

        // 리뷰의 수정 요청이 올바른지 검증
        Review review = validReview(reviewEditDto.getId());
        Byte oldStar = review.getStar();

        // 리뷰 수정
        review.changeInfo(reviewEditDto.getContent(), reviewEditDto.getStar());

        // 리뷰에서 별점이 수정되었다면 플랫폼 평점 업데이트 진행
        if(!oldStar.equals(review.getStar())) {
            refreshPlatformStar(review.getPlatform());
        }

        return review;
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = validReview(id);
        reviewRepository.delete(review);

        refreshPlatformStar(review.getPlatform());

    }



    /*
    * 플랫폼 평점 갱신
    * */
    private Platform refreshPlatformStar(Platform platform) {
        ReviewCountDto result = reviewRepository.findByStar(platform.getId());

        return platform.updateStar(result.getReviewCount(), result.getReviewTotalStar()); // 플랫폼 평점 업데이트
    }


    /*
     *  요청에 대한 멤버 검증 메서드
     */
    private Member validMember() {
        String memberName = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Member> member = memberRepository.findByUsername(memberName);

        if (member.isEmpty()) {
            throw new UsernameNotFoundException("요청에 해당하는 유저 정보를 찾을 수 없습니다.");
        }
        return member.get();
    }

    /*
    *   플랫폼 검증 메서드
     */
    private Platform validPlatform(Long platformId) {
        Optional<Platform> platform = platformRepository.findById(platformId);

        if (platform.isEmpty()) {
            throw new PlatformNotFoundException();
        }
        if (platform.get().getStatus() != PlatformStatus.ACCEPT) {
            throw new PlatformAccessDeniedException();
        }


        return platform.get();
    }

    /*
    * 리뷰 검증 메서드
    * */

    private Review validReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findByIdFetchOther(reviewId);

        if (review.isEmpty()) {
            throw  new ReviewNotFoundException();
        }

        // 리뷰를 작성한 사람과 요청하는 사람의 아이디가 다른 경우 예외 처리
        if (!SecurityContextHolder.getContext().getAuthentication().getName()
                .equals(review.get().getMember().getUsername())) {
            throw new ReviewAccessDeniedException();
        }

        if (review.get().getPlatform().getStatus() != PlatformStatus.ACCEPT) {
            throw new PlatformAccessDeniedException();
        }

        return review.get();
    }

}
