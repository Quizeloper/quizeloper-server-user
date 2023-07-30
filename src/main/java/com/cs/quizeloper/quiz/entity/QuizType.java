package com.cs.quizeloper.quiz.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum QuizType {
    MULTIPLE_CHOICE("객관식"),
    SHORT_ANSWER("주관식"),
    TF("True and False"),
    DD("Drag and Drop");
    private String value;

    QuizType(String value) {
        this.value = value;
    }

    public static QuizType getProviderByName(String value) {
        return Arrays.stream(QuizType.values())
                .filter(r -> r.getValue().equals(value))
                // todo: exception 처리 필요
                .findAny().orElse(null);
    }
}
