package com.cs.quizeloper.inquiry.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum InquiryType {
    QUESTION("건의사항"),
    OBJECTION("이의제기"),
    ETC("기타");

    private String value;

    InquiryType(String value) {
        this.value = value;
    }

    public static InquiryType getTypeByName(String value) {
        return Arrays.stream(InquiryType.values())
                .filter(r -> r.getValue().equals(value))
                // todo: exception 처리 필요
                .findAny().orElse(null);
    }
}
