package com.prs.ps.aop;

import static com.library.common.utils.CommonAopUtils.getParameterAnnotationList;

import com.prs.ps.annotation.ValidatePlatform;
import com.prs.ps.domain.Platform;
import com.prs.ps.exception.PlatformNotFoundException;
import com.prs.ps.repository.PlatformRepository;
import java.lang.annotation.Annotation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class PlatformValidateAspect {

    private final PlatformRepository platformRepository;

    @Around("execution(* *(.., @com.prs.ps.annotation.ValidatePlatform (*), ..))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable {

        Annotation[][] parameterAnnotations = getParameterAnnotationList(joinPoint);

        Object[] parameters = joinPoint.getArgs();

        // 파라미터 배열을 탐색하여 @ValidatePlatform 어노테이션이 있는 Long 타입의 파라미터를 찾아서 플랫폼 검증
        Loop:
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof ValidatePlatform
                    && parameters[i] instanceof Long platformId) {

                    Optional<Platform> platform = platformRepository.findById(platformId);
                    Integer platformParameterIdx = findParameterIdx(parameters);
                    parameters[platformParameterIdx] = platform.orElseThrow(
                        PlatformNotFoundException::new);

                    break Loop;
                }
            }
        }
        return joinPoint.proceed(parameters);
    }

    /*
     * Platform 타입이 있는 배열 인덱스 반환
     */
    private Integer findParameterIdx(Object[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] instanceof Platform) {
                return i;
            }
        }
        return -1;
    }
}
