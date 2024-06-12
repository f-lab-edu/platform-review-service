package com.prs.rs.service;


import com.prs.rs.domain.Review;
import com.prs.rs.dto.response.MemberInfoDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.exception.ReviewNotFoundException;
import com.prs.rs.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import com.prs.rs.dto.request.ReviewWriteDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.prs.rs.dto.request.ReviewEditDto;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewPersistenceManager {

    private final ReviewRepository reviewRepository;



    @Transactional
    public Long validateAndDeleteReview(Long reviewId) {
        Review review = validateReview(reviewId);

        reviewRepository.delete(review);

        // 플랫폼 평점 업데이트 로직이 들어가야 한다.
        // Platform platform = refreshPlatformStar(review.getPlatform());
        return 1L; // 업데이트 처리된 플랫폼 아이디 반환
    }


    @Transactional
    public Review validateAndWriteReview(ReviewWriteDto reviewWriteDto) {
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


    @Transactional
    public Review validateAndUpdateReview(ReviewEditDto reviewEditDto) {

        // 리뷰의 수정 요청이 올바른지 검증
        Review review = validateReview(reviewEditDto.getReviewId());
        Byte oldStar = review.getStar();

        // 리뷰 수정
        review.changeInfo(reviewEditDto.getContent(), reviewEditDto.getStar());

        // 리뷰에서 별점이 수정되었다면 플랫폼 평점 업데이트 진행
        if(!oldStar.equals(review.getStar())) {
             // refreshPlatformStar(review.getPlatform());

        }

        return review;
    }



    /*
     *  요청에 대한 멤버 검증 메서드
     */
    private MemberInfoDto validateMember() {
//        String memberName = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        Optional<Member> member = memberRepository.findByUsername(memberName);
//
//        if (member.isEmpty()) {
//            throw new UsernameNotFoundException("요청에 해당하는 유저 정보를 찾을 수 없습니다.");
//        }
        return null;
    }


    /*
     *   플랫폼 검증 메서드
     */
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


    /*
     * 리뷰 검증 메서드
     * */
    private Review validateReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if (review.isEmpty()) {
            throw  new ReviewNotFoundException();
        }

        // 리뷰를 작성한 사람과 요청하는 사람의 아이디가 다른 경우 예외 처리
        //        if (!SecurityContextHolder.getContext().getAuthentication().getName()
        //                .equals(review.get().getMember().getUsername())) {
        //            throw new ReviewAccessDeniedException();
        //        }


        // 플랫폼 상태가 ACCEPT 상태인지 검증하는 코드가 있어야 한다.

        return review.get();
    }
}
