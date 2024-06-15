package com.prs.ms.service;

import com.prs.ms.dto.MemberResponseDto;

public interface InternalMemberService {

    MemberResponseDto getMemberInfo();

    Boolean checkAdmin();
}
