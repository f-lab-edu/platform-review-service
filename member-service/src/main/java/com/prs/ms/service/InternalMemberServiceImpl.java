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
    public MemberResponseDto getMemberInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = validateMember(username);
        return new MemberResponseDto(member.getId(), member.getUsername());

    }
    



    @Override
    public Boolean checkAdmin() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(MemberRole.ROLE_ADMIN.role())) {
                return true;
            }
        }
        return false;
    }


    private Member validateMember(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        return member.orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원에 대한 요청입니다."));

    }
}
