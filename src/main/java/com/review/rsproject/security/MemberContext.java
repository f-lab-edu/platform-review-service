package com.review.rsproject.security;

import com.review.rsproject.dto.MemberDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class MemberContext extends User {

    public MemberContext(MemberDto memberDto, Collection<? extends GrantedAuthority> authorities) {
        super(memberDto.getUsername(), memberDto.getPassword(), authorities);
    }

}
