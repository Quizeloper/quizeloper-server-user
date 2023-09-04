package com.cs.quizeloper.quiz.Repository;

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


@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Page<Quiz> findAllByStatus(BaseStatus status, Pageable pageable);
    Page<Quiz> findAllByStatusAndStackUnit(BaseStatus status, Stack stack, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizUnitList q WHERE q.quiz.stackUnit = :stack and q.quizUnit.id in :unitRange and (q.quiz.type = :quizType or :quizType is null)")
    Page<Quiz> findAllByStackAndQuizUnitIsNullAndQuizTypeIsNull(Pageable pageable, Stack stack, QuizType quizType, @Param("unitRange") Long ... unitRange);
    @Query("SELECT q.quiz FROM QuizUnitList q WHERE (q.quiz.stackUnit = :stack or :stack is null) and (q.quiz.type = :quizType or :quizType is null)")
    Page<Quiz> findAllByStackAndQuizTypeIsNull(Pageable pageable, Stack stack, QuizType quizType);
}
