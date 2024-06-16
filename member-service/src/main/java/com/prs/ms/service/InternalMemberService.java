package com.prs.ms.service;

import com.prs.ms.dto.MemberResponseDto;

public interface InternalMemberService {

    MemberResponseDto findMemberInfo();
    MemberResponseDto findMemberInfoById(Long memberId);
    Boolean checkAdmin();
}
