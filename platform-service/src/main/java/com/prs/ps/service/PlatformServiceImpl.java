package com.prs.ps.service;


import com.library.validate.dto.MemberInfoDto;
import com.prs.ps.annotation.ValidatePlatform;
import com.prs.ps.common.ConstantValues;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.request.PlatformApplyDto;
import com.prs.ps.dto.request.PlatformEditDto;
import com.prs.ps.dto.request.PlatformSearchDto;
import com.prs.ps.dto.response.PlatformInfoDto;
import com.prs.ps.dto.response.PlatformPageDto;
import com.prs.ps.dto.response.PlatformRefreshDto;
import com.prs.ps.dto.response.PlatformSearchResultDto;
import com.prs.ps.repository.PlatformRepository;
import com.prs.ps.type.PlatformStatus;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.library.validate.annotation.ValidateMember;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {

    private final PlatformRepository platformRepository;

    @Override
    @Transactional
    public void refreshPlatformScore(@ValidatePlatform Long platformId, Platform platform,
        PlatformRefreshDto platformRefreshDto) {
        switch (platformRefreshDto.getAction()) {
            case CREATE -> platform.addScore(platformRefreshDto.getScore());
            case DELETE -> platform.subScore(platformRefreshDto.getScore());
            case UPDATE -> platform.updateScore(platformRefreshDto.getBeforeScore(),
                platformRefreshDto.getScore());
        }
    }



    @Override
    public Platform addPlatform(PlatformApplyDto applyDto,
        @ValidateMember MemberInfoDto memberInfoDto) {


        Platform platform = new Platform(applyDto.getName(), applyDto.getUrl(),
            applyDto.getDescription(), memberInfoDto.getMemberId());

        return platformRepository.save(platform);
    }

    @Override
    @Transactional
    public Platform updatePlatform(PlatformEditDto editDto, @ValidatePlatform Long platformId,
        Platform platform) {
        return platform.changeInfo(editDto.getDescription(), editDto.getStatus());
    }


    @Override
    public PlatformPageDto getPlatformList(Integer page, PlatformStatus status) {

        Pageable pageRequest = PageRequest.of(page, ConstantValues.PAGE_SIZE);

        Page<Platform> platform = platformRepository.findByStatus(pageRequest, status);

        // DTO 값 설정
        PlatformPageDto platformPageDto = new PlatformPageDto();
        platformPageDto.setNowPage(platform.getNumber());
        platformPageDto.setTotalPage(platform.getTotalPages());
        platformPageDto.setTotalSize(platform.getTotalElements());
        platformPageDto.setPageSize(platform.getSize());

        // entity to dto
        platform.getContent().forEach(p ->
            platformPageDto.getPlatformList()
                .add(new PlatformPageDto.Dto(p.getId(), p.getName(), p.getStatus()))
        );

        return platformPageDto;
    }

    @Override
    public PlatformInfoDto getPlatformInfo(@ValidatePlatform Long platformId, Platform platform,
                                            @ValidateMember MemberInfoDto memberInfoDto) {
        return PlatformInfoDto.builder()
            .platformName(platform.getName())
            .description(platform.getDescription())
            .memberName(memberInfoDto.getName())
            .url(platform.getUrl())
            .status(platform.getStatus())
            .modifiedDt(platform.getModifiedDt())
            .createdDt(platform.getCreatedDt()).build();
    }

    @Override
    public PlatformSearchResultDto getPlatformSearchResult(PlatformSearchDto platformSearchDto) {

        // 검색
        Page<Platform> result = performPlatformSearch(platformSearchDto);

        // 결과 반환
        return createSearchResultDto(result, platformSearchDto.getQuery());
    }


    private Page<Platform> performPlatformSearch(PlatformSearchDto platformSearchDto) {
        Pageable pageable = PageRequest.of(platformSearchDto.getPage(), ConstantValues.PAGE_SIZE);
        return platformRepository.findByQuery(platformSearchDto.getQuery(), pageable,
            platformSearchDto.getSort());
    }


    private PlatformSearchResultDto createSearchResultDto(Page<Platform> result, String query) {
        // dto 생성
        PlatformSearchResultDto searchResult = PlatformSearchResultDto.builder()
            .query(query)
            .nowPage(result.getNumber())
            .totalPage(result.getTotalPages())
            .platformCount(result.getContent().size())
            .platformList(new ArrayList<>())
            .totalSize(result.getTotalElements()).build();

        // entity -> dto 매핑
        for (Platform platform : result.getContent()) {
            PlatformSearchResultDto.Dto dto = PlatformSearchResultDto.Dto.builder()
                .platformId(platform.getId())
                .score(platform.getAvgScore())
                .reviewCount(platform.getReviewCount())
                .name(platform.getName())
                .description(platform.getDescription())
                .build();

            searchResult.getPlatformList().add(dto);
        }
        return searchResult;
    }


}
