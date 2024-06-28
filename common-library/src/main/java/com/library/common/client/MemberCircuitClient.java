package com.library.common.client;

import com.library.common.annotation.CookieSet;
import com.library.common.dto.MemberInfoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
@Slf4j
@CookieSet
@CircuitBreaker(name = "MemberService")
public class MemberCircuitClient implements MemberServiceClient {

    private final MemberServiceClient memberServiceClient;

    @Override
    public MemberInfoDto getMemberInfo() {
        return memberServiceClient.getMemberInfo();
    }

    @Override
    public MemberInfoDto getMemberInfoById(Long memberId) {
        return memberServiceClient.getMemberInfoById(memberId);
    }

    @Override
    public HashMap<Long, MemberInfoDto> getMembers(List<Long> memberIdList) {
        return memberServiceClient.getMembers(memberIdList);
    }

    @Override
    public Boolean checkAdmin() {
        return memberServiceClient.checkAdmin();
    }


}
