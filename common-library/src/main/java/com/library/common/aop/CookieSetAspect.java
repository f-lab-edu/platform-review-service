package com.library.common.aop;

import com.library.common.client.CookieHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = 1)
public class CookieSetAspect {

    @Around("@within(com.library.common.annotation.CookieSet)")
    public Object cookieSet(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        try {
            CookieHolder.setCookie();
            result = joinPoint.proceed(joinPoint.getArgs());
        } finally {
            CookieHolder.clear();
        }

        return result;
    }
}
