package com.prs.rs.service;

import static com.prs.rs.common.ConstantValues.PAGE_SIZE;
import static com.prs.rs.common.ConstantValues.PLATFORM_REFRESH_TOPIC;

import com.library.common.annotation.ValidateMember;
import com.library.common.client.MemberServiceClient;
import com.library.common.dto.MemberInfoDto;
import com.prs.rs.annotation.ValidatePlatform;
import com.prs.rs.annotation.ValidateReview;
import com.prs.rs.domain.Review;
import com.prs.rs.dto.request.PlatformRefreshDto;
import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.dto.response.ReviewListResultDto;
import com.prs.rs.event.KafkaProducer;
import com.prs.rs.exception.ReviewAccessDeniedException;
import com.prs.rs.repository.ReviewRepository;
import com.prs.rs.type.ActionStatus;
import com.prs.rs.type.SortType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {


    private final ReviewRepository reviewRepository;
    private final KafkaProducer kafkaProducer;
    private final MemberServiceClient memberServiceClient;
    private final CacheControlManager cacheControlManager;

    private static final String CACHE_GROUP = "reviews";

    private final Map<SortType, Sort> sortMap = new HashMap<>();

    {
        sortMap.put(SortType.STAR_ASC, Sort.by(Sort.Direction.ASC, "star"));
        sortMap.put(SortType.STAR_DESC, Sort.by(Sort.Direction.DESC, "star"));
        sortMap.put(SortType.DATE_ASC, Sort.by(Sort.Direction.ASC, "createdDt"));
        sortMap.put(SortType.DATE_DESC, Sort.by(Sort.Direction.DESC, "createdDt"));
    }


    @Override
    @Transactional
    public Review addReview(@ValidatePlatform Long platformId, PlatformInfoDto platformInfoDto,
        @ValidateMember MemberInfoDto memberInfoDto,
        ReviewWriteDto reviewWriteDto) {

        // 리뷰 저장
        Review review = new Review(platformInfoDto.getPlatformId(), memberInfoDto.getMemberId(),
            reviewWriteDto.getContent(), reviewWriteDto.getScore());
        reviewRepository.save(review);

        // 플랫폼 평점 업데이트
        updatePlatform(review.getPlatformId(), ActionStatus.CREATE, review.getScore());
        removeCache(review.getPlatformId());
        return review;
    }


    @Override
    public Review updateReview(@ValidateReview Long reviewId, Review review,
        @ValidateMember MemberInfoDto memberInfoDto,
        ReviewEditDto reviewEditDto) {

        checkAuthority(memberInfoDto, review);

        Integer beforeScore = review.getScore();

        // 리뷰 수정
        review.changeInfo(reviewEditDto.getContent(), reviewEditDto.getScore());
        reviewRepository.save(review);

        // 리뷰에서 별점이 수정되었다면 플랫폼 평점 업데이트 진행
        if (!beforeScore.equals(review.getScore())) {
            updatePlatform(review.getPlatformId(), review.getScore(), beforeScore);
        }
        removeCache(review.getPlatformId());
        return review;
    }


    @Override
    public Boolean deleteReview(@ValidateReview Long reviewId, Review review,
        @ValidateMember MemberInfoDto memberInfoDto) {
        try {
            checkAuthority(memberInfoDto, review);
        } catch (ReviewAccessDeniedException e) {
            // 어드민인지 체크
            if (!memberServiceClient.checkAdmin()) {
                throw new ReviewAccessDeniedException(e.getMessage());
            }
        }

        reviewRepository.delete(review);

        updatePlatform(review.getPlatformId(), ActionStatus.DELETE, review.getScore());
        removeCache(review.getPlatformId());

        return true;
    }


    @Override
    @Cacheable(value = "reviews",
        key = "#reviewListDto.platformId + '#' + #reviewListDto.page + '#' + #reviewListDto.sort",
        cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    public ReviewListResultDto getReviewList(ReviewListDto reviewListDto,
        @ValidatePlatform Long platformId, PlatformInfoDto platform) {

        Pageable pageRequest = PageRequest.of(reviewListDto.getPage(), PAGE_SIZE,
            sortMap.get(reviewListDto.getSort()));
        Page<Review> reviews = reviewRepository.findByIdFromPlatform(platform.getPlatformId(),
            pageRequest);

        return createReviewResultDto(platform, reviews);
    }

    private ReviewListResultDto createReviewResultDto(PlatformInfoDto platform,
        Page<Review> reviews) {
        ReviewListResultDto result = ReviewListResultDto.builder()
            .platformId(platform.getPlatformId())
            .platformName(platform.getName())
            .platformUrl(platform.getUrl())
            .platformDescription(platform.getDescription())
            .platformScore(platform.getScore())
            .totalReview(reviews.getTotalElements())
            .pageNo(reviews.getNumber())
            .reviewList(new ArrayList<>())
            .totalPage(reviews.getTotalPages()).build();

        HashMap<Long, MemberInfoDto> memberNameList = getReviewMemberList(reviews.getContent());

        for (Review review : reviews.getContent()) {

            MemberInfoDto memberInfoDto = memberNameList.get(review.getMemberId());

            ReviewListResultDto.Dto dto = ReviewListResultDto.Dto.builder()
                .reviewId(review.getId())
                .memberName(memberInfoDto.getName())
                .content(review.getContent())
                .score(review.getScore())
                .createdDt(review.getCreatedDt())
                .modifiedDt(review.getModifiedDt()).build();
            result.getReviewList().add(dto);
        }
        return result;
    }


    /*
     * 리뷰를 작성한 멤버의 이름을 가져온다.
     * */
    private HashMap<Long, MemberInfoDto> getReviewMemberList(List<Review> reviews) {

        List<Long> memberIdList = reviews.stream().map(Review::getMemberId).toList();

        return memberServiceClient.getMembers(memberIdList);
    }

    private void updatePlatform(Long platformId, ActionStatus actionStatus, Integer score) {

        PlatformRefreshDto platformRefreshDto = new PlatformRefreshDto(platformId, actionStatus,
            score);

        kafkaProducer.platformRefresh(PLATFORM_REFRESH_TOPIC, platformRefreshDto);
    }


    private void updatePlatform(Long platformId, Integer afterScore, Integer beforeScore) {

        PlatformRefreshDto platformRefreshDto = new PlatformRefreshDto(platformId,
            ActionStatus.UPDATE, afterScore, beforeScore);

        kafkaProducer.platformRefresh(PLATFORM_REFRESH_TOPIC, platformRefreshDto);
    }


    private void checkAuthority(MemberInfoDto memberInfoDto, Review review) {
        if (!memberInfoDto.getMemberId().equals(review.getMemberId())) {
            throw new ReviewAccessDeniedException();
        }
    }

    /*
     * 플랫폼 ID에 해당하는 리뷰 캐시 삭제
     */
    private void removeCache(Long platformId) {
        String pattern = CACHE_GROUP + "::" + platformId + "*";
        cacheControlManager.evictCacheByPattern(pattern);
    }


}
