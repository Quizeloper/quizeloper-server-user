package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
    Optional<QuizHistory> findFirstByQuizIdAndUserIdAndStatusOrderByCreatedDateDesc(long quizId, long userId, BaseStatus ACTIVE);
}
