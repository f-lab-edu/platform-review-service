package com.prs.rs.handler;

import com.prs.rs.exception.InvalidRequestException;
import com.prs.rs.exception.ReviewAccessDeniedException;
import com.prs.rs.exception.ReviewNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {


    /*
     * 커스텀 예외 처리
     * */
    @ExceptionHandler({ReviewNotFoundException.class, ReviewAccessDeniedException.class})
    public ResponseEntity<String> customEx500(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<String> customEx400(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
