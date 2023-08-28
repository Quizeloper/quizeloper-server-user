package com.cs.quizeloper.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    SUCCESS(HttpStatus.OK, "요청에 성공했습니다."),
    PAGE_COUNT_UNDER(HttpStatus.NOT_FOUND,"페이지는 0 이하로 입력할 수 없습니다."),
    PAGE_SIZE_COUNT_UNDER(HttpStatus.NOT_FOUND,"페이지에 들어가는 개수는 1 이하로 입력할 수 없습니다."),
    PAGE_COUNT_OVER(HttpStatus.NOT_FOUND,"조회 가능한 페이지수를 초과했습니다.");
    private HttpStatus status;
    private String message;

    BaseResponseStatus(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
