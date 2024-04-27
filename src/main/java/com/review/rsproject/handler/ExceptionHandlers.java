package com.review.rsproject.handler;

import com.review.rsproject.dto.ErrorDto;
import com.review.rsproject.exception.MemberSignUpException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlers {

    private final MessageSource messageSource;

    @ExceptionHandler(MemberSignUpException.class)
    public ResponseEntity<ErrorDto> signUpEx(MemberSignUpException ex) {
        ErrorDto errorDto = new ErrorDto("회원가입 실패", ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validEx(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        // 모든 필드 오류 가져오기
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // 각각의 필드 오류를 errors 에 put
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), messageSource.getMessage(fieldError.getCode(), null, null));
        }

        // 각 필드별 오류 내용을 반환
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

}
