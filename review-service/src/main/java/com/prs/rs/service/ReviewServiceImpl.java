package com.prs.rs.service;

import static com.prs.rs.common.ConstantValues.PAGE_SIZE;
import static com.prs.rs.common.ConstantValues.PLATFORM_REFRESH_TOPIC;

import com.prs.rs.annotation.ValidateMember;
import com.prs.rs.annotation.ValidatePlatform;
import com.prs.rs.annotation.ValidateReview;
import com.prs.rs.client.MemberServiceClient;
import com.prs.rs.domain.Review;
import com.prs.rs.dto.request.PlatformRefreshDto;
import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.MemberInfoDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.dto.response.ReviewListResultDto;
import com.prs.rs.event.KafkaProducer;
import com.prs.rs.exception.ReviewAccessDeniedException;
import com.prs.rs.repository.ReviewRepository;
import com.prs.rs.type.SortType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final KafkaProducer kafkaProducer;
    private final MemberServiceClient memberServiceClient;

    private final Map<SortType, Sort> sortMap = new HashMap<>();

    {
        sortMap.put(SortType.STAR_ASC, Sort.by(Sort.Direction.ASC, "star"));
        sortMap.put(SortType.STAR_DESC, Sort.by(Sort.Direction.DESC, "star"));
        sortMap.put(SortType.DATE_ASC, Sort.by(Sort.Direction.ASC, "createdDt"));
        sortMap.put(SortType.DATE_DESC, Sort.by(Sort.Direction.DESC, "createdDt"));
    }


    @Override
    public Review addReview(@ValidatePlatform Long platformId, PlatformInfoDto platformInfoDto,
        @ValidateMember MemberInfoDto memberInfoDto,
        ReviewWriteDto reviewWriteDto) {

        // 리뷰 저장
        Review review = new Review(platformInfoDto.getPlatformId(), memberInfoDto.getMemberId(),
            reviewWriteDto.getContent(), reviewWriteDto.getStar());
        reviewRepository.save(review);

        // 플랫폼 평점 업데이트
        updatePlatform(review.getPlatformId());

        return review;
    }


    @Override
    public Review updateReview(@ValidateReview Long reviewId, Review review,
        @ValidateMember MemberInfoDto memberInfoDto,
        ReviewEditDto reviewEditDto) {

        checkAuthority(memberInfoDto, review);

        Byte beforeStar = review.getStar();

        // 리뷰 수정
        review.changeInfo(reviewEditDto.getContent(), reviewEditDto.getStar());
        reviewRepository.save(review);

        // 리뷰에서 별점이 수정되었다면 플랫폼 평점 업데이트 진행
        if (!beforeStar.equals(review.getStar())) {
            updatePlatform(review.getPlatformId());
        }
        return review;
    }


    @Override
    public void deleteReview(@ValidateReview Long reviewId, Review review,
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

        updatePlatform(review.getPlatformId());
    }


    @Override
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
            .platformNo(platform.getPlatformId())
            .platformName(platform.getName())
            .platformUrl(platform.getUrl())
            .platformDescription(platform.getDescription())
            .platformStar(platform.getStar())
            .totalReview(reviews.getTotalElements())
            .pageNo(reviews.getNumber())
            .reviewList(new ArrayList<>())
            .totalPage(reviews.getTotalPages()).build();

        HashMap<Long, MemberInfoDto> memberNameList = getReviewMemberList(reviews.getContent());

        for (Review review : reviews.getContent()) {

            MemberInfoDto memberInfoDto = memberNameList.get(review.getMemberId());

            ReviewListResultDto.Dto dto = ReviewListResultDto.Dto.builder()
                .reviewNumber(review.getId())
                .memberName(memberInfoDto.getName())
                .content(review.getContent())
                .star(review.getStar())
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


    private void updatePlatform(Long platformId) {
        PlatformRefreshDto platformRefreshDto = reviewRepository.findByIdAndFetchInfo(platformId);
        kafkaProducer.platformRefresh(PLATFORM_REFRESH_TOPIC, platformRefreshDto);
    }


    private void checkAuthority(MemberInfoDto memberInfoDto, Review review) {
        if (!memberInfoDto.getMemberId().equals(review.getMemberId())) {
            throw new ReviewAccessDeniedException();
        }
    }


}
