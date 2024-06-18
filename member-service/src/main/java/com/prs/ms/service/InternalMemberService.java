package com.prs.ms.service;

import com.prs.ms.dto.MemberResponseDto;
import java.util.HashMap;
import java.util.List;

public interface InternalMemberService {

    MemberResponseDto findMemberInfo();

    MemberResponseDto findMemberInfoById(Long memberId);

    HashMap<Long, MemberResponseDto> findMembers(List<Long> memberList);

    Boolean checkAdmin();
}
