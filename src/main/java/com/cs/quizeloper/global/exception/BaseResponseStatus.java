package com.cs.quizeloper.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    SUCCESS(HttpStatus.OK, "요청에 성공했습니다.");
    private HttpStatus status;
    private String message;

    BaseResponseStatus(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
