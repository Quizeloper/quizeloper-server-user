package com.cs.quizeloper.user.dto;

import com.cs.quizeloper.quiz.entity.QuizDashboard;
import com.cs.quizeloper.quiz.entity.Stack;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MyPageStackRes {
    private String stack;

    public static MyPageStackRes toDto(QuizDashboard quizDashboards){
        return MyPageStackRes.builder()
                .stack(quizDashboards.getStackUnit().getValue())
                .build();
    }
}
