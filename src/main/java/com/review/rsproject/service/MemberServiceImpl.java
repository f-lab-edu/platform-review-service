package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.MemberDto;
import com.review.rsproject.exception.MemberSignUpException;
import com.review.rsproject.repository.MemberRepository;
import com.review.rsproject.type.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberDto memberDto) {


        // 유저 이름 중복여부 체크
        duplicate(memberDto.getUsername());


        // 패스워드 암호화 및 유저 엔티티 생성
        String password = passwordEncoder.encode(memberDto.getPassword());
        Member member = new Member(memberDto.getUsername(), password, MemberRole.ROLE_USER);

        // DB에 저장
        return memberRepository.save(member);

    }


    public void duplicate(String username) {
        Optional<Member> findUsername = memberRepository.findByUsername(username);
        if (findUsername.isPresent()) {
            throw new MemberSignUpException();
        }
    }

}
