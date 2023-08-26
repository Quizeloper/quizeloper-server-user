package com.cs.quizeloper.quiz.entity;

import com.cs.quizeloper.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
public class QuizUnitList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "quiz_unit_id")
    private QuizUnit quizUnit;

    @Column(nullable = false)
    private String unitName;

    @Builder
    public QuizUnitList(QuizUnit quizUnit, String unitName){
        this.quizUnit = quizUnit;
        this.unitName = unitName;
    }
}
