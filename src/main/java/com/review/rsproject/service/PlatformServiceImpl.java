package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.PlatformApplyDto;
import com.review.rsproject.dto.PlatformEditDto;
import com.review.rsproject.exception.PlatformNotFoundException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            throw new PlatformNotFoundException("존재하지 않는 플랫폼 번호입니다.");
        }


        return platform.get().changeInfo(editDto.getDescription(), editDto.getStatus());
    }


}
