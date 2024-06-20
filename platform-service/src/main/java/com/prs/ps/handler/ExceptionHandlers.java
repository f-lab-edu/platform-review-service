package com.prs.ps.handler;

import com.prs.ps.exception.PlatformAccessDeniedException;
import com.prs.ps.exception.PlatformCreationException;
import com.prs.ps.exception.PlatformNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    /*
     * 커스텀 예외 처리
     * */
    @ExceptionHandler({PlatformCreationException.class, PlatformNotFoundException.class,
        PlatformAccessDeniedException.class})
    public ResponseEntity<String> customEx500(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    
}
