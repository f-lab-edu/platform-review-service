package com.prs.ms.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("존재하지 않는 회원에 대한 요청입니다.");
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
