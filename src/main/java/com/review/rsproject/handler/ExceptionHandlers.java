package com.review.rsproject.handler;

import com.review.rsproject.dto.ErrorDto;
import com.review.rsproject.exception.MemberSignUpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<ErrorDto> signUpEx(MemberSignUpException memberSignUpException) {
        ErrorDto errorDto = new ErrorDto("회원가입 실패", memberSignUpException.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

}
