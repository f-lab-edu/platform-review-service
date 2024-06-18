package com.prs.ms.service;

import com.prs.ms.domain.Member;
import com.prs.ms.dto.MemberRegisterDto;

public interface MemberService {

    Member register(MemberRegisterDto memberRegisterDto);
}
