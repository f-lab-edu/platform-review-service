package com.review.rsproject.common;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

public class CommonUtils {

    private static final String USERNAME = "test_user";


     public static void setContextByUsername(String username) {
        UserDetails userDetails = new User(username, "123123", new ArrayList<>());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    }

    public static Platform mockBuildPlatform() {
        Member member = new Member(USERNAME, "1111", MemberRole.ROLE_USER);
        Platform platform =new Platform("네이버", "https://naver.com", "네이버버버버", member);
        return platform.changeInfo(platform.getDescription(), PlatformStatus.ACCEPT);
    }
}
