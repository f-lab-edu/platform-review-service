package com.library.common.client;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CookieHolder {

    private static final InheritableThreadLocal<Cookie[]> COOKIE = new InheritableThreadLocal<>();


    // 현재 쓰레드의 쿠키 값을 자식 쓰레드에서 사용할 수 있도록 저장
    public static void setCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        COOKIE.set(request.getCookies());
    }

    public static Cookie[] getCookie() {
        return COOKIE.get();
    }

    public static void clear() {
        COOKIE.remove();
    }
}
