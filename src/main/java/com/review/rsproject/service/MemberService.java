package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.MemberDto;

public interface MemberService {

    Member register(MemberDto memberDto);

}
