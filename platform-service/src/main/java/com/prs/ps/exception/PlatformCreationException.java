package com.prs.ps.exception;

public class PlatformCreationException extends RuntimeException {


    public PlatformCreationException() {
        super("플랫폼을 생성하는 과정에서 오류가 발생하였습니다.");
    }

    public PlatformCreationException(String message) {
        super(message);
    }
}
