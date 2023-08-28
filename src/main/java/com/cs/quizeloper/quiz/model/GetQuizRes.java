package com.cs.quizeloper.quiz.model;

import com.cs.quizeloper.quiz.entity.QuizType;
import com.cs.quizeloper.quiz.entity.QuizUnit;
import com.cs.quizeloper.quiz.entity.QuizUnitList;
import com.cs.quizeloper.quiz.entity.Stack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetQuizRes {
    private Long quizId;
    private String title;
    private QuizType type;
    private Stack stackUnit;
    private List<String> quizUnitLists;
    private boolean likeYN;

}
