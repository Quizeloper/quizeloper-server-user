package com.cs.quizeloper.quiz.model;

import com.cs.quizeloper.quiz.entity.QuizType;
import com.cs.quizeloper.quiz.entity.QuizUnit;
import com.cs.quizeloper.quiz.entity.Stack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetQuizRes {
    private String title;
    private QuizType type;
    private Stack stackUnit;
    private Long quizUnitId;
    private boolean likeYN;

}
