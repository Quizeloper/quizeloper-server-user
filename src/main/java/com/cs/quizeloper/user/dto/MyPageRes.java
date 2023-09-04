package com.cs.quizeloper.user.dto;

import com.cs.quizeloper.quiz.entity.QuizDashboard;
import com.cs.quizeloper.quiz.entity.Stack;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MyPageRes {
    private String email;
    private String nickname;
    private List<MyPageStackRes> category;
    private MyPageQuestionRes questions;

    public static MyPageRes toDto(String email, String nickname, List<QuizDashboard> dashboards, MyPageQuestionRes myPageQuestionRes){
        return MyPageRes.builder()
                .email(email)
                .nickname(nickname)
                .category(dashboards.stream().map(MyPageStackRes::toDto).collect(Collectors.toList()))
                .questions(myPageQuestionRes)
                .build();
    }
}
