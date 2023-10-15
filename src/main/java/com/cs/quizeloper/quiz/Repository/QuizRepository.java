package com.cs.quizeloper.quiz.repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.Quiz;
import com.cs.quizeloper.quiz.entity.QuizType;
import com.cs.quizeloper.quiz.entity.Stack;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Optional<Quiz> findByIdAndStatus(Long quizIdx, BaseStatus status);
    Page<Quiz> findAllByStatus(BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizUnitList q WHERE q.quiz.stackUnit = :stack and q.quizUnit.id in :unitRange and (q.quiz.type = :quizType or :quizType is null)")
    Page<Quiz> findAllByStackAndQuizUnitIsNullAndQuizTypeIsNull(Pageable pageable, Stack stack, QuizType quizType, @Param("unitRange") Long ... unitRange);
    @Query("SELECT q.quiz FROM QuizUnitList q WHERE (q.quiz.stackUnit = :stack or :stack is null) and (q.quiz.type = :quizType or :quizType is null)")
    Page<Quiz> findAllByStackAndQuizTypeIsNull(Pageable pageable, Stack stack, QuizType quizType);
    @Query("SELECT COUNT(DISTINCT (CASE WHEN q.stackUnit= :stack THEN q.id END)) FROM Quiz q WHERE q.status = :status")
    Double findAllStacks(Stack stack, BaseStatus status);
}
