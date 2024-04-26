package com.review.rsproject.security;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.MemberDto;
import com.review.rsproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> findUsername = memberRepository.findByUsername(username);

        if (findUsername.isEmpty()) {
            throw new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다.");
        }
        Member member = findUsername.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().role()));

        return new User(member.getUsername(), member.getPassword(), authorities);

    }
}
