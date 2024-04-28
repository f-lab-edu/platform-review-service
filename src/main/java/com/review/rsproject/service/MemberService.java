package com.review.rsproject.service;

import com.review.rsproject.domain.Member;
import com.review.rsproject.dto.MemberRegisterDto;

public interface MemberService {

    Member register(MemberRegisterDto memberRegisterDto);

}
