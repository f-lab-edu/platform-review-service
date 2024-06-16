package com.prs.ps.service;


import com.prs.ps.annotation.ValidatePlatform;
import com.prs.ps.client.MemberServiceClient;
import com.prs.ps.common.ConstantValues;
import com.prs.ps.domain.Platform;
import com.prs.ps.dto.request.PlatformApplyDto;
import com.prs.ps.dto.request.PlatformEditDto;
import com.prs.ps.dto.request.PlatformSearchDto;
import com.prs.ps.dto.response.MemberInfoDto;
import com.prs.ps.dto.response.PlatformInfoDto;
import com.prs.ps.dto.response.PlatformPageDto;
import com.prs.ps.dto.response.PlatformSearchResultDto;
import com.prs.ps.exception.PlatformNotFoundException;
import com.prs.ps.repository.PlatformRepository;
import com.prs.ps.type.PlatformStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService {

    private final PlatformRepository platformRepository;
    private final MemberServiceClient memberServiceClient;

    @Override
    public Platform addPlatform(PlatformApplyDto applyDto) {

        MemberInfoDto memberInfo = memberServiceClient.getMemberInfo();

        Platform platform = new Platform(applyDto.getName(), applyDto.getUrl(), applyDto.getDescription(), memberInfo.getMemberId());

        return platformRepository.save(platform);
    }

    @Override
    @Transactional
    public Platform updatePlatform(PlatformEditDto editDto, @ValidatePlatform Long platformId, Platform platform) {
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
                platformPageDto.getPlatformList().add(new PlatformPageDto.Dto(p.getId(), p.getName(), p.getStatus()))
        );

        return platformPageDto;
    }

    @Override
    public PlatformInfoDto getPlatformInfo(@ValidatePlatform Long platformId, Platform platform) {
        MemberInfoDto memberInfo = memberServiceClient.getMemberInfoById(platform.getMemberId());
        return PlatformInfoDto.builder()
                .platformName(platform.getName())
                .description(platform.getDescription())
                .memberName(memberInfo.getName())
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
        return platformRepository.findByQuery(platformSearchDto.getQuery(), pageable, platformSearchDto.getSort());
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


        // 리뷰 갯수를 가져오는 로직이 구현되어 있어야 한다.

        // entity -> dto 매핑
        for (Platform platform : result.getContent()) {
            PlatformSearchResultDto.Dto dto = PlatformSearchResultDto.Dto.builder()
                    .reviewNumber(platform.getId())
                    .star(platform.getStar())
                    .reviewCount(1)
                    .name(platform.getName())
                    .description(platform.getDescription())
                    .build();

            searchResult.getPlatformList().add(dto);
        }
        return searchResult;
    }


}
