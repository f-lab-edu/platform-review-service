package com.review.rsproject.exception;

public class PlatformAccessDeniedException extends RuntimeException{

    public PlatformAccessDeniedException() {
        super("플랫폼에 대한 접근이 거부되었습니다.");
    }

    public PlatformAccessDeniedException(String message) {
        super(message);
    }
}
