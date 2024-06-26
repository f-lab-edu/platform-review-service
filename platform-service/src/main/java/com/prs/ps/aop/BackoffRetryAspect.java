package com.prs.ps.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class BackoffRetryAspect {

    private final static int MAX_RETRIES = 3;
    private final static int INITIAL_DELAY = 500;
    private final static int MAX_DELAY = 3000;

    @Around("@annotation(com.prs.ps.annotation.Retry)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        for (int attempt = 0; attempt < MAX_RETRIES; ++attempt) {
            try {
                result = joinPoint.proceed();
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                long delay = (long) Math.min(INITIAL_DELAY * Math.pow(2, attempt), MAX_DELAY);
                log.info("[{}] {}ms 후 다시 시도", joinPoint.getSignature().getName(), delay);
                Thread.sleep(delay);
            }
        }
        return result;

    }
}
