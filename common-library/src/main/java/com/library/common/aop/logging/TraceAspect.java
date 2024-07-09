package com.library.common.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TraceAspect {

    private ThreadLocal<Integer> depth = new ThreadLocal<>();
    private final int FIRST_REQUEST = 1;

    @Around("execution(* *..*Controller.*(..)) || execution(* *..*Service.*(..))")
    public Object logBeforeMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getName();
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        Object result = null;
        String prefix = getPrefix();
        try {

            log.info("{}> {}.{}()", prefix, packageName, methodName);
            result = joinPoint.proceed();
        } finally {
            if (depth.get().equals(FIRST_REQUEST)) {
                depth.remove();
            }

            log.info("<{} {}.{}()", prefix, packageName, methodName);
        }
        return result;
    }

    private String getPrefix() {
        if (depth.get() == null) {
            depth.set(FIRST_REQUEST);
        } else {
            depth.set(depth.get().intValue() + 1);
        }

        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < depth.get(); ++i) {
            prefix.append("──");
        }
        return prefix.toString();
    }


}
