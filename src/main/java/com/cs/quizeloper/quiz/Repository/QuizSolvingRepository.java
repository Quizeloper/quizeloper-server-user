package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.quiz.entity.QuizSolving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSolvingRepository extends JpaRepository<QuizSolving, Long> {
}
