package com.review.rsproject.service;

import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.request.PlatformApplyDto;
import com.review.rsproject.dto.request.PlatformEditDto;
import com.review.rsproject.dto.request.SearchDto;
import com.review.rsproject.dto.response.PlatformInfoDto;
import com.review.rsproject.dto.response.PlatformPageDto;
import com.review.rsproject.dto.response.PlatformSearchDto;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import com.review.rsproject.type.PlatformSort;
import com.review.rsproject.type.PlatformStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
                platformPageDto.getPlatformList().add(new PlatformPageDto.dto(p.getId(), p.getName(), p.getStatus()))
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
    public PlatformSearchDto getPlatformSearchResult(SearchDto searchDto) {


        Pageable pageable = PageRequest.of(searchDto.getPage(), ConstantValues.PAGE_SIZE);

        Page<Platform> result = platformRepository.findByQuery(searchDto.getQuery(), pageable, searchDto.getSort());

        // dto 생성
        PlatformSearchDto searchResult = PlatformSearchDto.builder()
                .query(searchDto.getQuery())
                .nowPage(result.getNumber())
                .totalPage(result.getTotalPages())
                .pageSize(result.getSize())
                .platformList(new ArrayList<>())
                .totalSize(result.getTotalElements()).build();

        // entity -> dto 매핑
        for (Platform platform : result.getContent()) {
            PlatformSearchDto.dto dto = PlatformSearchDto.dto.builder()
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
