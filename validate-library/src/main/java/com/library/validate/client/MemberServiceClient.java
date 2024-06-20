package com.library.validate.client;

import com.library.validate.dto.MemberInfoDto;
import java.util.HashMap;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service")
@Component
public interface MemberServiceClient {

    @GetMapping("/api/in/member")
    MemberInfoDto getMemberInfo();

    @GetMapping("/api/in/members")
    HashMap<Long, MemberInfoDto> getMembers(
        @RequestParam(name = "memberIdList") List<Long> memberIdList);

    @GetMapping("/api/in/auth/check-admin")
    Boolean checkAdmin();

}
