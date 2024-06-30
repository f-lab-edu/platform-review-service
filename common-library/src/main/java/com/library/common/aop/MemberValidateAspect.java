package com.library.common.aop;

import static com.library.common.utils.CommonAopUtils.getParameterAnnotationList;

import com.library.common.client.MemberCircuitClient;
import com.library.common.dto.MemberInfoDto;
import java.lang.annotation.Annotation;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.library.common.annotation.ValidateMember;


@Aspect
@Component
@RequiredArgsConstructor
public class MemberValidateAspect {

    private final MemberCircuitClient memberServiceClient;

    @Around("execution(* *(.., @com.library.common.annotation.ValidateMember (*), ..))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {

        Annotation[][] parameterAnnotations = getParameterAnnotationList(joinPoint);

        Object[] parameters = joinPoint.getArgs(); // 파라미터

        // 파라미터 배열을 탐색하여 @ValidateMember 어노테이션이 있는 MemberInfoDto 타입의 파라미터를 찾아서 검증
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
