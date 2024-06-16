package com.prs.ms.service;

import com.prs.ms.domain.Member;
import com.prs.ms.dto.MemberResponseDto;
import com.prs.ms.exception.MemberNotFoundException;
import com.prs.ms.repository.MemberRepository;
import com.prs.ms.type.MemberRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalMemberServiceImpl implements InternalMemberService {


    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDto findMemberInfo() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = validateMember(username);

        return new MemberResponseDto(member.getId(), member.getUsername());

    }

    @Override
    public MemberResponseDto findMemberInfoById(Long memberId) {
        Member member = validateMember(memberId);

        return new MemberResponseDto(member.getId(), member.getUsername());
    }





    @Override
    public Boolean checkAdmin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Member member = validateMember(username);

       return member.getRole() == MemberRole.ROLE_ADMIN;
    }


    private Member validateMember(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        return member.orElseThrow(MemberNotFoundException::new);
    }

    private Member validateMember(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(MemberNotFoundException::new);

    }
}
