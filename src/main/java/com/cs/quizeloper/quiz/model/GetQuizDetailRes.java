package com.cs.quizeloper.quiz.model;

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
public class GetQuizDetailRes {
    private String title;
    private String content;
    private QuizType type;
    private Stack stackUnit;
    private List<String> unitLists;
    private List<GetSolvingRes> quizSolving;
    private boolean likeYN;

    public static GetQuizDetailRes toDto(Quiz quiz, List<String> unitLists, List<GetSolvingRes> quizSolving, boolean likeYN) {
        return GetQuizDetailRes.builder()
                .title(quiz.getTitle())
                .content(quiz.getContent())
                .type(quiz.getType())
                .stackUnit(quiz.getStackUnit())
                .unitLists(unitLists)
                .quizSolving(quizSolving)
                .likeYN(likeYN)
                .build();
    }
}
