package com.cs.quizeloper.quiz.entity;

import com.cs.quizeloper.global.entity.BaseEntity;
import com.cs.quizeloper.user.entity.User;
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
public class QuizHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizStatus quizStatus;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "quiz_id")
    private Quiz quiz;

    @Builder
    public QuizHistory(QuizStatus quizStatus, User user, Quiz quiz) {
        this.quizStatus = quizStatus;
        this.user = user;
        this.quiz = quiz;
    }

    public static QuizHistory toEntity(QuizStatus quizStatus, Quiz quiz, User user){
        return QuizHistory.builder()
                .quizStatus(quizStatus)
                .user(user)
                .quiz(quiz)
                .build();
    }
}
