package com.review.rsproject.service;

import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.request.PlatformApplyDto;
import com.review.rsproject.dto.request.PlatformEditDto;
import com.review.rsproject.dto.request.PlatformSearchDto;
import com.review.rsproject.dto.response.PlatformInfoDto;
import com.review.rsproject.dto.response.PlatformPageDto;
import com.review.rsproject.dto.response.PlatformSearchResultDto;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.type.PlatformStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlatformServiceImpl implements PlatformService{

    private final MemberRepository memberRepository;
    private final PlatformRepository platformRepository;

    @Override
    public Platform addPlatform(PlatformApplyDto applyDto) {

       String username = SecurityContextHolder.getContext().getAuthentication().getName();
       Optional<Member> member = memberRepository.findByUsername(username);

       if (member.isEmpty()) {
           throw new UsernameNotFoundException("요청에 대한 유저 정보를 조회할 수 없습니다.");
       }

        Platform platform = new Platform(applyDto.getName(), applyDto.getUrl(), applyDto.getDescription(), member.get());

        return platformRepository.save(platform);
    }

    @Override
    @Transactional
    public Platform updatePlatform(PlatformEditDto editDto) {

        Long id = editDto.getId();
        Optional<Platform> platform = platformRepository.findById(id);

        if (platform.isEmpty()) {
            throw new PlatformNotFoundException();
        }


        return platform.get().changeInfo(editDto.getDescription(), editDto.getStatus());
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
    public PlatformInfoDto getPlatformInfo(Long id) {
        Optional<Platform> platform = platformRepository.findByIdAndFetchMember(id);

        if (platform.isEmpty()) {
            throw new PlatformNotFoundException();
        }

        Platform result = platform.get();

        return PlatformInfoDto.builder()
                .platformName(result.getName())
                .description(result.getDescription())
                .memberName(result.getMember().getUsername())
                .url(result.getUrl())
                .status(result.getStatus())
                .modifiedDt(result.getModifiedDt())
                .createdDt(result.getCreatedDt()).build();
    }

    @Override
    @Cacheable(value = "search_results", key = "#platformSearchDto.query + '#' + #platformSearchDto.page", cacheManager = "redisCacheManager")
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

        // entity -> dto 매핑
        for (Platform platform : result.getContent()) {
            PlatformSearchResultDto.Dto dto = PlatformSearchResultDto.Dto.builder()
                    .reviewNumber(platform.getId())
                    .star(platform.getStar())
                    .reviewCount(platform.getReviews().size())
                    .name(platform.getName())
                    .description(platform.getDescription())
                    .build();

            searchResult.getPlatformList().add(dto);
        }
        return searchResult;
    }


}
