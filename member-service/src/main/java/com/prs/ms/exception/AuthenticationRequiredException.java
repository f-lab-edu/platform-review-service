package com.prs.ms.exception;

public class AuthenticationRequiredException extends RuntimeException {


    public AuthenticationRequiredException() {
        super("로그인이 필요한 요청입니다.");
    }

    public AuthenticationRequiredException(String message) {
        super(message);
    }

}
