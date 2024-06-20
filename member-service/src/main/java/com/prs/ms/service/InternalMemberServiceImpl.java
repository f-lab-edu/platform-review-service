package com.prs.ms.service;

import com.prs.ms.domain.Member;
import com.prs.ms.dto.MemberResponseDto;
import com.prs.ms.exception.MemberNotFoundException;
import com.prs.ms.repository.MemberRepository;
import com.prs.ms.type.MemberRole;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InternalMemberServiceImpl implements InternalMemberService {


    private final MemberRepository memberRepository;


    /*
     * 요청하는 멤버의 정보 반환
     * */
    @Override
    public MemberResponseDto findMemberInfo() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = validateMember(username);

        return new MemberResponseDto(member.getId(), member.getUsername());
    }


    /*
     * 특정 멤버의 정보 반환
     * */
    @Override
    public MemberResponseDto findMemberInfoById(Long memberId) {
        Member member = validateMember(memberId);

        return new MemberResponseDto(member.getId(), member.getUsername());
    }


    /*
     * 다수의 멤버 정보 반환
     */
    @Override
    public HashMap<Long, MemberResponseDto> findMembers(List<Long> memberIdList) {
        List<Member> members = memberRepository.findByIdIn(memberIdList);

        // List<Member> -> HashMap<Long, MemberResponseDto>
        return (HashMap<Long, MemberResponseDto>) members.stream().
            collect(Collectors.toMap(Member::getId,
                m -> new MemberResponseDto(m.getId(), m.getUsername())));
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
