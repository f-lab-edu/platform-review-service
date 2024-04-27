package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.dto.PlatformApplyDto;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.repository.PlatformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
           throw new UsernameNotFoundException("잘못된 요청");
       }

        Platform platform = new Platform(applyDto.getName(), applyDto.getUrl(), applyDto.getDescription(), member.get());

        return platformRepository.save(platform);
    }
}
