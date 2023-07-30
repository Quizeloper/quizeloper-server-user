package com.cs.quizeloper.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler({BaseException.class})
    protected ResponseEntity<BaseResponse<BaseResponseStatus>> handleBaseException(BaseException e){
        return new ResponseEntity<>(new BaseResponse<>(e.getStatus()), e.getStatus().getStatus());
    }
}
