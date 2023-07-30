package com.cs.quizeloper.quiz.entity;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Stack {
    JS("Java Script"),
    SPRING("Spring boot"),
    DEVOPS("Devops");

    private String value;

    Stack(String value) {
        this.value = value;
    }

    public static Stack getProviderByName(String value) {
        return Arrays.stream(Stack.values())
                .filter(r -> r.getValue().equals(value))
                // todo: exception 처리 필요
                .findAny().orElse(null);
    }
}
