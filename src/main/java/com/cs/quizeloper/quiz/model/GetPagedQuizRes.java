package com.cs.quizeloper.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetPagedQuizRes {
    private List<GetQuizRes> QuizList;
    private int total;
    private int page;


}
