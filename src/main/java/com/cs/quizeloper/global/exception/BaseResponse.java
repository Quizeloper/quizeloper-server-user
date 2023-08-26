package com.cs.quizeloper.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import static com.cs.quizeloper.global.exception.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {
    private final String message;
    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse(T result) {
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getStatus().value();
        this.result = result;
    }

    public BaseResponse(BaseResponseStatus status) {
        this.message = status.getMessage();
        this.code = status.getStatus().value();
    }

    public BaseResponse(int code, String message) {
        this.message = message;
        this.code = code;
    }
}
