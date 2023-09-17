package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
    Optional<QuizHistory> findFirstByQuizIdAndUserIdAndStatusOrderByCreatedDateDesc(long quizId, long userId, BaseStatus ACTIVE);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and q.quiz.stackUnit = :stack and q.user = :user order by q.createdDate desc")
    Page<Quiz> findAllByOrderByCreatedDateDesc(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and q.quiz.stackUnit = :stack and q.user = :user order by q.createdDate asc")
    Page<Quiz> findAllByOrderByCreatedDateAsc(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and q.quiz.stackUnit = :stack and q.user = :user GROUP BY q.quiz order by count(q.quiz) desc ")
    Page<Quiz> findAllByLarge(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and q.quiz.stackUnit = :stack and q.user = :user GROUP BY q.quiz order by count(q.quiz) asc ")
    Page<Quiz> findAllBySmall(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quiz.stackUnit = :stack and q.user = :user")
    Page<Quiz> findAll(User user, Stack stack, BaseStatus status, Pageable pageable);

}
