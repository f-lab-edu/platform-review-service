package com.library.common.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;

public class FeignCookieInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {

        Cookie[] cookeStore = CookieHolder.getCookie();

        if (cookeStore != null && cookeStore.length > 0) {
            StringBuilder cookieHeaders = new StringBuilder();
            for (Cookie cookie : cookeStore) {
                cookieHeaders.append(cookie.getName() + "=" + cookie.getValue() + ";");
            }
            requestTemplate.header("Cookie", String.valueOf(cookieHeaders));
        }
    }
}
