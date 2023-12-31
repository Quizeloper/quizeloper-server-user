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
    @JoinColumn(nullable = false, name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "quiz_unit_id")
    private QuizUnit quizUnit;

    @Builder
    public QuizUnitList(Quiz quiz, QuizUnit quizUnit){
        this.quiz = quiz;
        this.quizUnit = quizUnit;
    }
}
