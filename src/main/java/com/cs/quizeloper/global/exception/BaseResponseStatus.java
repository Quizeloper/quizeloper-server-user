package com.cs.quizeloper.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    SUCCESS(HttpStatus.OK, "요청에 성공했습니다."),
    // jwt
    NULL_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 값을 입력해주세요."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 값입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 형식의 토큰 값입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 구조의 토큰 값입니다."),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "만료된 토큰 값입니다."),
    REQUIRED_AUTH_ANNOTATION(HttpStatus.FORBIDDEN, "auth annotation 을 붙여주세요."),
    NOT_ACCESS_HEADER(HttpStatus.INTERNAL_SERVER_ERROR, "헤더에 접근할 수 없습니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다. "),

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
