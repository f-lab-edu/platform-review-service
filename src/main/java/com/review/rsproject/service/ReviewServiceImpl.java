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
import com.review.rsproject.exception.PlatformAccessDeniedException;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.exception.ReviewAccessDeniedException;
import com.review.rsproject.exception.ReviewNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.repository.ReviewRepository;
import com.review.rsproject.type.PlatformStatus;
import com.review.rsproject.type.SortType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        Member member = validateMember();
        Platform platform = validatePlatform(reviewWriteDto.getId());


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
        Review review = validateReview(reviewEditDto.getId());
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
        Review review = validateReview(id);
        reviewRepository.delete(review);

        refreshPlatformStar(review.getPlatform());

    }

    @Override
    public ReviewListResultDto getReviewList(ReviewListDto reviewListDto) {
        Platform platform = validatePlatform(reviewListDto.getId());
        
        Pageable pageRequest = PageRequest.of(reviewListDto.getPage(), ConstantValues.PAGE_SIZE, sortConverter(reviewListDto.getSort()));
        Page<Review> reviews = reviewRepository.findByIdFromPlatform(platform.getId(), pageRequest);


        return createReviewResultDto(platform, reviews);
    }

    private ReviewListResultDto createReviewResultDto(Platform platform, Page<Review> reviews) {
        ReviewListResultDto result = ReviewListResultDto.builder()
                .platformNo(platform.getId())
                .platformName(platform.getName())
                .platformUrl(platform.getUrl())
                .platformDescription(platform.getDescription())
                .platformStar(platform.getStar())
                .totalReview(reviews.getTotalElements())
                .pageNo(reviews.getNumber())
                .reviewList(new ArrayList<>())
                .totalPage(reviews.getTotalPages()).build();

        for (Review review : reviews.getContent()) {
            ReviewListResultDto.Dto dto = ReviewListResultDto.Dto.builder()
                    .reviewNumber(review.getId())
                    .memberName(review.getMember().getUsername())
                    .content(review.getContent())
                    .star(review.getStar())
                    .createdDt(review.getCreatedDt())
                    .modifiedDt(review.getModifiedDt()).build();
            result.getReviewList().add(dto);
        }
        return result;
    }

    /*
    * SortType -> Sort
    * */
    private Sort sortConverter(SortType sort) {
        switch (sort) {
            case STAR_ASC ->  { return Sort.by(Sort.Direction.ASC, "star"); }
            case STAR_DESC ->  { return Sort.by(Sort.Direction.DESC, "star"); }
            case DATE_DESC -> { return Sort.by(Sort.Direction.DESC, "createdDt"); }
            default ->  { return Sort.by(Sort.Direction.ASC, "createdDt"); }
        }
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
    private Member validateMember() {
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
    private Platform validatePlatform(Long platformId) {
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

    private Review validateReview(Long reviewId) {
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
