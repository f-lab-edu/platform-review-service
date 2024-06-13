package com.prs.ms.service;

import com.prs.ms.domain.Member;
import com.prs.ms.dto.MemberRegisterDto;
import com.prs.ms.dto.MemberResponseDto;

public interface MemberService {

    Member register(MemberRegisterDto memberRegisterDto);

    MemberResponseDto getMemberInfo();
}
