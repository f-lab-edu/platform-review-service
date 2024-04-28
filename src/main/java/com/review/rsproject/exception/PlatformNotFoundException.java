package com.review.rsproject.exception;

public class PlatformNotFoundException extends RuntimeException{

    public PlatformNotFoundException() {
    }

    public PlatformNotFoundException(String message) {
        super(message);
    }
}
