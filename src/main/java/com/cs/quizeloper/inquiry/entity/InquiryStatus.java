package com.cs.quizeloper.inquiry.entity;

import com.cs.quizeloper.quiz.entity.QuizType;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum InquiryStatus {
    WAITING("답변대기"),
    REPLIED("답변완료");

    private String value;

    InquiryStatus(String value) {
        this.value = value;
    }

    public static InquiryStatus getProviderByName(String value) {
        return Arrays.stream(InquiryStatus.values())
                .filter(r -> r.getValue().equals(value))
                // todo: exception 처리 필요
                .findAny().orElse(null);
    }
}
