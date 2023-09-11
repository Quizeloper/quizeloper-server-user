package com.cs.quizeloper.quiz.model;

import com.cs.quizeloper.quiz.entity.QuizUnit;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetQuizUnitRes {
    private long quizUnitId;
    private String stack;
    private String unitName;

    public static GetQuizUnitRes toDto(QuizUnit quizUnit){
        return GetQuizUnitRes.builder()
                .quizUnitId(quizUnit.getId())
                .stack(quizUnit.getStack().getValue())
                .unitName(quizUnit.getUnit())
                .build();
    }
}
