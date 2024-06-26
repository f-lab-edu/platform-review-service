package com.prs.rs.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
        super("올바르지 못한 요청입니다.");
    }

    public InvalidRequestException(String message) {
        super(message);
    }

}
