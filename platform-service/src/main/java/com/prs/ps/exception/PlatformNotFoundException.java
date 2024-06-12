package com.prs.ps.exception;

public class PlatformNotFoundException extends RuntimeException{

    public PlatformNotFoundException() {
        super("존재하지 않는 플랫폼입니다.");
    }

    public PlatformNotFoundException(String message) {
        super(message);
    }
}
