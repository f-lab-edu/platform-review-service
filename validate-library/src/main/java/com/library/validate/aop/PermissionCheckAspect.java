package com.library.validate.aop;

import com.library.validate.client.MemberServiceClient;
import com.library.validate.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionCheckAspect {

    private final MemberServiceClient memberServiceClient;


    @Before("@annotation(com.library.validate.annotation.RequiresPermission)")
    public void check() {
        if (!memberServiceClient.checkAdmin()) {
            throw new AccessDeniedException();
        }
    }
}
