package com.prs.ps.client;

import com.prs.ps.dto.response.MemberInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
@Component
public interface MemberServiceClient {


    @GetMapping("/api/in/member")
    MemberInfoDto getMemberInfo();

    @GetMapping("/api/in/members/{memberId}")
    MemberInfoDto getMemberInfoById(@PathVariable("memberId") Long memberId);

    @GetMapping("/api/in/auth/check-admin")
    Boolean checkAdmin();

}
