package com.cs.quizeloper.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPagedQuizRes {
    private List<GetQuizRes> quizResList;
    private int total;
    private int page;
    private boolean nextPage;
}
