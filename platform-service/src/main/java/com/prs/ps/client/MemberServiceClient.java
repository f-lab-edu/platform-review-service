package com.prs.ps.client;

import com.prs.ps.dto.response.MemberInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@FeignClient(name = "member-service")
@Component
public interface MemberServiceClient {
    @GetMapping("/api/member")
    MemberInfoDto getMemberInfo();

}
