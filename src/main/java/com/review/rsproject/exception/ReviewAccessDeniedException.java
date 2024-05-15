package com.review.rsproject.exception;

public class ReviewAccessDeniedException extends RuntimeException {

    public ReviewAccessDeniedException() {
        super("해당 리뷰에 접근할 수 있는 권한이 없습니다.");

    }

    public ReviewAccessDeniedException(String message) {
        super(message);
    }
}
