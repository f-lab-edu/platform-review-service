package com.prs.rs.client;


import com.prs.rs.dto.response.PlatformInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "platform-service")
@Component
public interface PlatformServiceClient {

    @GetMapping("/api/in/platforms/{platformId}")
    PlatformInfoDto getPlatformInfo(@PathVariable("platformId") Long platformId);
}
