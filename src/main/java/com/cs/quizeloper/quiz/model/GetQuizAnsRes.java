package com.cs.quizeloper.quiz.model;

import com.cs.quizeloper.quiz.entity.Quiz;
import com.cs.quizeloper.quiz.entity.QuizHistory;
import com.cs.quizeloper.quiz.entity.QuizStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetQuizAnsRes {
    private long quizId;
    private String answer;
    private String solving;
    private QuizStatus quizStatus;

    public static GetQuizAnsRes toDto(Quiz quiz, QuizHistory quizHistory){
        return GetQuizAnsRes.builder()
                .quizId(quiz.getId())
                .answer(quiz.getAnswer())
                .solving(quiz.getSolving())
                .quizStatus(quizHistory.getQuizStatus())
                .build();
    }
}
