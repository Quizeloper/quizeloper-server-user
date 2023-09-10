package com.cs.quizeloper.user.dto;

import com.cs.quizeloper.quiz.entity.Stack;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPageQuestionRes {
    private Integer questionNum;
    private Integer solvedQuestionNum;

    public static MyPageQuestionRes toDto(Integer questionNum, Integer solvedQuestionNum){
        return MyPageQuestionRes.builder()
                .questionNum(questionNum)
                .solvedQuestionNum(solvedQuestionNum)
                .build();
    }
}
