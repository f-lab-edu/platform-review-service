package com.prs.rs.client;

import com.library.common.annotation.CookieSet;
import com.prs.rs.dto.response.PlatformInfoDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@CookieSet
@CircuitBreaker(name = "PlatformService")
@RequiredArgsConstructor
@Component
public class PlatformCircuitClient implements PlatformServiceClient {

    private final PlatformServiceClient platformServiceClient;

    @Override
    public PlatformInfoDto getPlatformInfo(Long platformId) {
        return platformServiceClient.getPlatformInfo(platformId);
    }
}
