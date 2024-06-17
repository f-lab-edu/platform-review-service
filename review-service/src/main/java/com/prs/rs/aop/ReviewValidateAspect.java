package com.prs.rs.aop;


import com.prs.rs.annotation.ValidateReview;
import com.prs.rs.domain.Review;
import com.prs.rs.dto.response.MemberInfoDto;
import com.prs.rs.dto.response.PlatformInfoDto;
import com.prs.rs.exception.ReviewNotFoundException;
import com.prs.rs.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;


@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ReviewValidateAspect {


    private final ReviewRepository reviewRepository;

    @Around("execution(* *(.., @com.prs.rs.annotation.ValidateReview (*), ..))")
    public Object validate(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations(); // 파라미터에 달린 어노테이션 가져오기

        Object[] parameters = joinPoint.getArgs(); // 파라미터


        // 파라미터 배열을 탐색하여 @ValidateReview 어노테이션이 있는 Long 타입의 파라미터를 찾아서 검증
        Loop : for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof ValidateReview && parameters[i] instanceof Long reviewId) {

                    Optional<Review> findReview = reviewRepository.findById(reviewId);
                    Review review = findReview.orElseThrow(ReviewNotFoundException::new);

                    Integer parameterIdx = findParameterIdx(parameters);
                    parameters[parameterIdx] = review;

                    break Loop;
                }
            }
        }
        return joinPoint.proceed(parameters);
    }

    /*
     * Review 타입이 있는 배열 인덱스 반환
     */
    private Integer findParameterIdx(Object[] parameters) {
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i] instanceof Review) {
                return i;
            }
        }
        return -1;
    }
}
