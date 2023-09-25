package com.cs.quizeloper.user.dto;

import com.cs.quizeloper.quiz.entity.Quiz;
import com.cs.quizeloper.quiz.entity.QuizType;
import com.cs.quizeloper.quiz.entity.Stack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserQuizHistoryRes {
    private Long quizId;
    private String title;
    private QuizType type;
    private Stack stackUnit;
    private List<String> quizUnitLists;
    private Boolean likeYN;

    public static GetUserQuizHistoryRes toDto(Quiz quiz, List<String> quizLists, Boolean likeYN){
        return GetUserQuizHistoryRes.builder()
                .quizId(quiz.getId())
                .title(quiz.getTitle())
                .type(quiz.getType())
                .stackUnit(quiz.getStackUnit())
                .quizUnitLists(quizLists)
                .likeYN(likeYN)
                .build();
    }
}
