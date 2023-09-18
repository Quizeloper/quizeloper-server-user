package com.cs.quizeloper.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetMyQuizSummary {
    private Double progressRate;
    private Double answerRate;
    private Integer tryNum;
    private Integer solNum;
    private Integer successNum;
    private Integer failureNum;

    public static GetMyQuizSummary toDto(List<Double> rateList, Integer total, Integer solNum, Integer successNum, Integer failureNum) {
        return GetMyQuizSummary.builder()
                .progressRate(rateList.get(0))
                .answerRate(rateList.get(1))
                .tryNum(total)
                .solNum(solNum)
                .successNum(successNum)
                .failureNum(failureNum)
                .build();
    }
}
