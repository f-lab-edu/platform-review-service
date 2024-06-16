package com.prs.rs.client;

import com.prs.rs.dto.response.MemberInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "member-service")
@Component
public interface MemberServiceClient {

    @GetMapping("/api/in/member")
    MemberInfoDto getMemberInfo();

    @GetMapping("/api/in/auth/check-admin")
    Boolean checkAdmin();

}
