package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.domain.Review;
import com.review.rsproject.dto.request.ReviewWriteDto;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        Platform platform = validPlatform(reviewWriteDto.getPlatformId());

        // 리뷰 저장
        Review review = new Review(platform, member, reviewWriteDto.getContent(), reviewWriteDto.getStar());
        reviewRepository.save(review);

        // 플랫폼 평점 업데이트
        Platform updatedPlatform = refreshPlatformStar(platform);
        platformRepository.save(updatedPlatform);

        return review;
    }

    /*
    * 플랫폼 평점 갱신
    * */
    private Platform refreshPlatformStar(Platform platform) {
        List<Long[]> result = reviewRepository.findByStar(platform.getId());
        Long reviewCount = result.get(0)[0]; // 달린 리뷰의 수
        Long reviewTotalStar = result.get(0)[1]; // 리뷰 점수의 합계

        return platform.updateStar(reviewCount, reviewTotalStar); // 플랫폼 평점 업데이트
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
        return platform.get();
    }
}
