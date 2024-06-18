package com.prs.rs.aop;

import com.prs.rs.annotation.ValidateMember;
import com.prs.rs.client.MemberServiceClient;
import com.prs.rs.dto.response.MemberInfoDto;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberValidateAspect {

    private final MemberServiceClient memberServiceClient;

    @Around("execution(* *(.., @com.prs.rs.annotation.ValidateMember (*), ..))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations(); // 파라미터에 달린 어노테이션 가져오기

        Object[] parameters = joinPoint.getArgs(); // 파라미터

        // 파라미터 배열을 탐색하여 @ValidatePlatform 어노테이션이 있는 PlatformInfoDto 타입의 파라미터를 찾아서 검증
        Loop:
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof ValidateMember
                    && parameters[i] instanceof MemberInfoDto) {

                    MemberInfoDto memberInfo = memberServiceClient.getMemberInfo();
                    parameters[i] = memberInfo;

                    break Loop;
                }
            }
        }
        return joinPoint.proceed(parameters);
    }
}
