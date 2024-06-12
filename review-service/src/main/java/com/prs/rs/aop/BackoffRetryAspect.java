package com.prs.rs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import static com.prs.rs.common.ConstantValues.*;

@Aspect
@Slf4j
@Component
public class BackoffRetryAspect {


    @Around("@annotation(com.prs.rs.aop.Retry)")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result = null;
        for (int attempt = 0; attempt < MAX_RETRIES; ++attempt) {
            try {
                result = joinPoint.proceed();
                break;
            }
            catch (ObjectOptimisticLockingFailureException e) {
                long delay = (long) Math.min(INITIAL_DELAY * Math.pow(2, attempt) , MAX_DELAY);
                log.info("[{}] {}ms 후 다시 시도", joinPoint.getSignature().getName(), delay);
                Thread.sleep(delay);
            }
        }
        return result;

    }
}
