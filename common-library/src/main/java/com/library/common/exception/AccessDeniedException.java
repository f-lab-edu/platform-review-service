package com.library.common.exception;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException() {
        super("접근이 권한이 부족합니다.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

}
