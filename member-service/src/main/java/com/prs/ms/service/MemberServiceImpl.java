package com.prs.ms.service;

import com.prs.ms.dto.MemberRegisterDto;
import com.prs.ms.dto.MemberResponseDto;
import com.prs.ms.exception.MemberNotFoundException;
import com.prs.ms.exception.MemberSignUpException;
import com.prs.ms.repository.MemberRepository;
import com.prs.ms.type.MemberRole;
import com.prs.ms.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterDto memberRegisterDto) {


        // 유저 이름 중복여부 체크
        duplicate(memberRegisterDto.getUsername());


        // 패스워드 암호화 및 유저 엔티티 생성
        String password = passwordEncoder.encode(memberRegisterDto.getPassword());
        Member member = new Member(memberRegisterDto.getUsername(), password, MemberRole.ROLE_USER);

        // DB에 저장
        return memberRepository.save(member);

    }


    private void duplicate(String username) {
        Optional<Member> findUsername = memberRepository.findByUsername(username);
        if (findUsername.isPresent()) {
            throw new MemberSignUpException("이미 존재하는 회원 이름입니다.");
        }
    }

    private Member validateMember(String username) {
        Optional<Member> member = memberRepository.findByUsername(username);
        return member.orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원에 대한 요청입니다."));

    }


    @Override
    public MemberResponseDto getMemberInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member = validateMember(username);
        return new MemberResponseDto(member.getId(), member.getUsername());

    }


}
