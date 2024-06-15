package com.prs.ps.aop;

import com.prs.ps.client.MemberServiceClient;
import com.prs.ps.dto.response.MemberInfoDto;
import com.prs.ps.exception.PlatformAccessDeniedException;
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


    @Before("@annotation(com.prs.ps.annotation.RequiresPermission)")
    public void check() {
        MemberInfoDto memberInfo = memberServiceClient.getMemberInfo();

        if (!memberInfo.getAdmin()) {
            throw new PlatformAccessDeniedException();
        }

    }
}
