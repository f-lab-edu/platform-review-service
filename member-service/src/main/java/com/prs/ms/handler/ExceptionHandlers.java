package com.prs.ms.handler;


import com.prs.ms.exception.AuthenticationRequiredException;
import com.prs.ms.exception.MemberNotFoundException;
import com.prs.ms.exception.MemberSignUpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {


    /*
     * 커스텀 예외 처리
     * */
    @ExceptionHandler({MemberSignUpException.class, MemberNotFoundException.class})
    public ResponseEntity<String> customEx500(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({AuthenticationRequiredException.class})
    public ResponseEntity<String> customEx400(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
