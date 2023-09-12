package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.QuizSolving;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizSolvingRepository extends JpaRepository<QuizSolving, Long> {

    List<QuizSolving> findAllByQuizIdAndStatus(long quizId, BaseStatus status);
}
