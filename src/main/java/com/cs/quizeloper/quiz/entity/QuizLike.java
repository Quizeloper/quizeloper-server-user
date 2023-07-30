package com.cs.quizeloper.quiz.entity;

import com.cs.quizeloper.global.entity.BaseEntity;
import com.cs.quizeloper.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ManyToAny;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
public class QuizLike extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "quiz_id")
    private Quiz quiz;

    @Builder
    public QuizLike(User user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
    }
}
