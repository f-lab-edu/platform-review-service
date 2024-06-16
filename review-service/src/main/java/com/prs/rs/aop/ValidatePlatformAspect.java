package com.prs.rs.aop;

import com.prs.rs.client.PlatformServiceClient;
import com.prs.rs.dto.response.PlatformInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;


@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatePlatformAspect {

    private final PlatformServiceClient platformServiceClient;

    @Around("execution(* *(.., @com.prs.rs.aop.ValidatePlatform (*), ..))")
    public Object retry(ProceedingJoinPoint joinPoint) throws Throwable{


        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations(); // 파라미터에 달린 어노테이션 가져오기

        Object[] parameters = joinPoint.getArgs(); // 파라미터


        // 파라미터 배열을 탐색하여 @ValidatePlatform 어노테이션이 있는 PlatformInfoDto 타입의 파라미터를 찾아서 검증
        Loop : for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof ValidatePlatform && parameters[i] instanceof Long platformId) {

                    PlatformInfoDto platformInfo = platformServiceClient.getPlatformInfo(platformId);

                    Integer platformParameterIdx = findParameterIdx(parameters);

                    parameters[platformParameterIdx] = platformInfo;

                    break Loop;
                }
            }
        }
        return joinPoint.proceed(parameters);
    }


    /*
     * PlatformInfoDto 타입이 있는 배열 인덱스 반환
     */
    private Integer findParameterIdx(Object[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i] instanceof PlatformInfoDto) {
                return i;
            }
        }
        return -1;
    }
}
