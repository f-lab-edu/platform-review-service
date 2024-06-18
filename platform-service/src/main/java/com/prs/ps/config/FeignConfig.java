package com.prs.ps.config;

import com.prs.ps.client.FeignCookieInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor feignCookieInterceptor() {
        return new FeignCookieInterceptor();
    }

}