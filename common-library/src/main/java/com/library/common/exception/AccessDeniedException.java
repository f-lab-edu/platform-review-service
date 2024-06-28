package com.library.common.exception;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException() {
        super("접근이 거부되었습니다.");
    }

    public AccessDeniedException(String message) {
        super(message);
    }

}