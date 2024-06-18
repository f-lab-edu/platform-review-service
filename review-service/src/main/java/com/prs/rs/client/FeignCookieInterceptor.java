package com.prs.rs.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignCookieInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Cookie[] cookeStore = request.getCookies();

        if (cookeStore != null && cookeStore.length > 0) {
            StringBuilder cookieHeaders = new StringBuilder();
            for (Cookie cookie : cookeStore) {
                cookieHeaders.append(cookie.getName() + "=" + cookie.getValue() + ";");
            }
            requestTemplate.header("Cookie", String.valueOf(cookieHeaders));
        }
    }
}
