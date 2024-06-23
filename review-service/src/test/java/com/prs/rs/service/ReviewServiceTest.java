package com.prs.rs.service;

import com.library.common.client.MemberServiceClient;
import com.library.common.dto.MemberInfoDto;
import com.prs.rs.domain.Review;
import com.prs.rs.dto.request.ReviewEditDto;
import com.prs.rs.dto.request.ReviewListDto;
import com.prs.rs.dto.request.ReviewWriteDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.dto.response.ReviewListResultDto;
import com.prs.rs.event.KafkaProducer;
import com.prs.rs.exception.ReviewAccessDeniedException;
import com.prs.rs.repository.ReviewRepository;
import com.prs.rs.type.SortType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static com.prs.rs.common.ConstantValues.PAGE_SIZE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {


    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private MemberServiceClient memberServiceClient;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private CacheControlManager cacheControlManager;


    @Test
    @DisplayName("addReview() - 리뷰 작성 테스트")
    void addReviewTest() {
        // given
        PlatformInfoDto requestPlatformDto = new PlatformInfoDto();
        requestPlatformDto.setPlatformId(1L);

        MemberInfoDto requestMemberDto = getMockMember("testUser");

        ReviewWriteDto request = new ReviewWriteDto(1L, "sfllfsdlflsdlflsdf", 3);

        // when
        Review review = reviewService.addReview(requestPlatformDto.getPlatformId(),
            requestPlatformDto, requestMemberDto, request);

        // then
        Assertions.assertThat(review.getPlatformId()).isEqualTo(requestPlatformDto.getPlatformId());
        Assertions.assertThat(review.getContent()).isEqualTo(request.getContent());
        Assertions.assertThat(review.getScore()).isEqualTo(request.getScore());

    }

    @Test
    @DisplayName("updateReviewTest() - 리뷰 수정 테스트")
    void updateReviewTest() {
        // given
        MemberInfoDto requestMemberDto = getMockMember("testUser");

        Review writtenReview = getMockReview(1L, requestMemberDto.getMemberId());

        ReviewEditDto request = new ReviewEditDto(writtenReview.getId(), "수정된 리뷰", 7);

        // when
        Review updatedReview = reviewService.updateReview(writtenReview.getId(), writtenReview,
            requestMemberDto, request);

        // then
        Assertions.assertThat(updatedReview.getContent()).isEqualTo(request.getContent());
        Assertions.assertThat(updatedReview.getScore()).isEqualTo(request.getScore());


    }

    @Test
    @DisplayName("updateReviewEx() - 리뷰 작성자와 수정자가 다를 경우")
    void updateReviewEx() {
        // given
        MemberInfoDto requestMemberDto = getMockMember("testUser");

        Review writtenReview = getMockReview(1L, requestMemberDto.getMemberId() + 1);
        when(writtenReview.getId()).thenReturn(1L);

        ReviewEditDto request = new ReviewEditDto(writtenReview.getId(), "수정된 리뷰", 7);

        // when & then
        Assertions.assertThatThrownBy(
            () -> reviewService.updateReview(writtenReview.getId(), writtenReview,
                requestMemberDto, request)).isInstanceOf(ReviewAccessDeniedException.class);


    }


    @Test
    @DisplayName("getReviewListTest() - 리뷰 조회 테스트")
    void getReviewListTest() {
        // given
        int reviewTotalCount = 13; // 총 리뷰 갯수

        PlatformInfoDto mockPlatform = getMockPlatform("네이버"); // 가짜 플랫폼 생성

        List<Review> reviewList = getMockReviewList(mockPlatform.getPlatformId(), // 가짜 리뷰 생성
            10);

        ReviewListDto request = new ReviewListDto(mockPlatform.getPlatformId(), 0,
            SortType.DATE_DESC); // 요청 객체

        /* stub */
        Pageable pageable = PageRequest.of(request.getPage(), PAGE_SIZE,
            Sort.by(Sort.Direction.DESC, "createdDt"));
        Page<Review> reviews = new PageImpl<>(reviewList, pageable, reviewTotalCount);

        when(reviewRepository.findByIdFromPlatform(any(), any())).thenReturn(reviews);

        HashMap<Long, MemberInfoDto> members = new HashMap<>();
        for (Review review : reviewList) {
            MemberInfoDto memberInfoDto = new MemberInfoDto(review.getMemberId(),
                "testUser" + review.getId());
            members.put(review.getMemberId(), memberInfoDto);
        }
        when(memberServiceClient.getMembers(any())).thenReturn(members);

        // when
        ReviewListResultDto result = reviewService.getReviewList(request,
            mockPlatform.getPlatformId(), mockPlatform);

        // then
        Assertions.assertThat(result.getPlatformId()).isEqualTo(request.getPlatformId()); // 플랫폼 아이디
        Assertions.assertThat(result.getPageNo()).isEqualTo(request.getPage()); // 리뷰 페이지
        Assertions.assertThat(result.getTotalReview()).isEqualTo(reviewTotalCount); // 리뷰 총 갯수

        Long[] resultReviewIdList = result.getReviewList().stream().map(r -> r.getReviewId())
            .toArray(Long[]::new);
        Long[] reviewIdList = reviewList.stream().map(r -> r.getId()).toArray(Long[]::new);

        Assertions.assertThat(resultReviewIdList)
            .containsOnly(reviewIdList); // 리뷰 아이디들이 잘 가져와졌는지 확인


    }

    @Test
    @DisplayName("deleteReviewTest() - 리뷰 삭제 테스트")
    void deleteReviewTest() {
        // given
        Review writtenReview = getMockReview(1L, 1L);

        MemberInfoDto member = new MemberInfoDto(writtenReview.getMemberId(), "testUser23");

        // when
        Boolean result = reviewService.deleteReview(writtenReview.getId(), writtenReview, member);

        // then
        Assertions.assertThat(result).isEqualTo(true);
    }


    @Test
    @DisplayName("deleteReviewEx1() - 리뷰 삭제를 요청하는 사람이 작성자가 아닌 경우")
    void deleteReviewEx1() {
        // given
        Review writtenReview = getMockReview(1L, 1L);

        MemberInfoDto differentMember = new MemberInfoDto(writtenReview.getMemberId() + 1,
            "testUser23");

        when(memberServiceClient.checkAdmin()).thenReturn(false);

        // when & then
        Assertions
            .assertThatThrownBy(
                () -> reviewService.deleteReview(writtenReview.getId(), writtenReview,
                    differentMember))
            .isInstanceOf(ReviewAccessDeniedException.class);
    }


    @Test
    @DisplayName("deleteReviewAdminTest() - 리뷰 삭제를 요청하는 사람이 작성자가 아니지만 어드민 권한을 가졌을 경우")
    void deleteReviewAdminTest() {
        // given
        Review writtenReview = getMockReview(1L, 1L);

        MemberInfoDto differentMember = new MemberInfoDto(writtenReview.getMemberId() + 1,
            "testUser23");
        when(memberServiceClient.checkAdmin()).thenReturn(true);

        // when
        Boolean result = reviewService.deleteReview(writtenReview.getId(), writtenReview,
            differentMember);

        // then
        Assertions.assertThat(result).isEqualTo(true);
    }


    private MemberInfoDto getMockMember(String username) {
        return new MemberInfoDto((long) username.hashCode(), username);
    }

    private Review getMockReview(Long platformId, Long memberId) {
        Review review = spy(new Review(platformId, memberId, "작성된 테스트 리뷰", 5));
        lenient().when(review.getId()).thenReturn((long) UUID.randomUUID().toString().hashCode());
        return review;
    }

    private List<Review> getMockReviewList(Long platformId, Integer count) {
        List<Review> reviewList = new ArrayList<>();
        for (int i = 1; i <= count; ++i) {
            Review mockReview = getMockReview(platformId, i + 1000L);
            reviewList.add(mockReview);
        }
        return reviewList;
    }

    private PlatformInfoDto getMockPlatform(String platformName) {
        PlatformInfoDto platformInfo = spy(new PlatformInfoDto());
        lenient().when(platformInfo.getPlatformId()).thenReturn((long) platformName.hashCode());
        lenient().when(platformInfo.getUrl()).thenReturn("https://naver.com");
        lenient().when(platformInfo.getName()).thenReturn(platformName);
        lenient().when(platformInfo.getScore()).thenReturn(5);
        lenient().when(platformInfo.getDescription()).thenReturn("검색 엔진 플랫폼입니다.");
        return platformInfo;
    }
}