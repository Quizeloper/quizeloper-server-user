package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.quiz.entity.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
}
