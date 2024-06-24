package com.library.common.config;

import com.library.common.client.FeignCookieInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {


    @Bean
    public FeignCookieInterceptor feignCookieInterceptor() {
        return new FeignCookieInterceptor();
    }

}