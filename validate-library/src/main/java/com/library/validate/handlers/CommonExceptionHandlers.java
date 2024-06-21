package com.library.validate.handlers;


import com.library.validate.exception.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class CommonExceptionHandlers {


    private final MessageSource messageSource;

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<String> customEx(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /*
     * 요청 매핑 실패 예외 처리
     * */
    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class,
        MissingServletRequestParameterException.class})
    public ResponseEntity<String> requestEx(Exception ex) {
        return new ResponseEntity<>("요청 형식이 맞지 않습니다.", HttpStatus.BAD_REQUEST);
    }


    /*
     * Bean Validation 에 대한 검증 예외 처리
     * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validEx(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 모든 필드 오류 가져오기
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        // 각각의 필드 오류를 errors 에 put
        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(),
                messageSource.getMessage(fieldError.getCode(), null, null));
        }

        // 각 필드별 오류 내용을 반환
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }
}