package com.library.validate.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class CommonAopUtils {

    /*
    * 파라미터에 달린 어노테이션 가져오기
    * */
    public static Annotation[][] getParameterAnnotationList(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        return method.getParameterAnnotations();
    }

}
