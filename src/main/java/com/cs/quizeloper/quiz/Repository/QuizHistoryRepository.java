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
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and (:stack IS NULL OR q.quiz.stackUnit = :stack) and q.user = :user order by q.createdDate desc")
    Page<Quiz> findAllByOrderByCreatedDateDesc(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and (:stack IS NULL OR q.quiz.stackUnit = :stack) and q.user = :user order by q.createdDate asc")
    Page<Quiz> findAllByOrderByCreatedDateAsc(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and (:stack IS NULL OR q.quiz.stackUnit = :stack) and q.user = :user GROUP BY q.quiz order by count(q.quiz) desc ")
    Page<Quiz> findAllByLarge(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and q.quizStatus = :quizStatus and (:stack IS NULL OR q.quiz.stackUnit = :stack) and q.user = :user GROUP BY q.quiz order by count(q.quiz) asc ")
    Page<Quiz> findAllBySmall(User user, Stack stack, QuizStatus quizStatus, BaseStatus status, Pageable pageable);
    @Query("SELECT q.quiz FROM QuizHistory q WHERE q.status = :status and (:stack IS NULL OR q.quiz.stackUnit = :stack) and q.user = :user")
    Page<Quiz> findAll(User user, Stack stack, BaseStatus status, Pageable pageable);

    @Query("SELECT COUNT(qh) FROM QuizHistory as qh WHERE qh.status = :status and qh.user = :user and FUNCTION('DATE', qh.createdDate) = FUNCTION('DATE', :date)")
    Integer findAllHistoryNum(User user, String date, BaseStatus status);

    @Query("SELECT COUNT(DISTINCT qh.quiz) FROM QuizHistory as qh WHERE qh.status = :status and qh.user = :user and FUNCTION('DATE', qh.createdDate) = FUNCTION('DATE', :date)")
    Integer findSolvedHistoryNum(User user, String date, BaseStatus status);
    @Query("""
            SELECT COUNT(DISTINCT qh.quiz) as successNum
            FROM QuizHistory as qh WHERE qh.status = :status and qh.user = :user AND FUNCTION('DATE', qh.createdDate) = FUNCTION('DATE', :date) AND qh.quizStatus = 'SUCCESS'""")
    Integer findSuccessHistory(User user, String date, BaseStatus status);

    @Query("""
            SELECT COUNT(DISTINCT qh.quiz) as failureNum
            FROM QuizHistory qh WHERE qh.status = :status and qh.user = :user AND FUNCTION('DATE', qh.createdDate) = FUNCTION('DATE', :date) AND qh.quizStatus = 'FAILURE'""")
    Integer findFailureHistory(User user, String date, BaseStatus status);
    @Query("SELECT COUNT(DISTINCT qh.quiz) FROM QuizHistory qh WHERE qh.status = :status and qh.user = :user and qh.quiz.stackUnit = :stack")
    Double findSolveHistoryByStacks(User user, Stack stack, BaseStatus status);

    @Query("SELECT COUNT(DISTINCT qh.quiz)\n" +
            "FROM QuizHistory qh\n" +
            "WHERE qh.status = :status\n" +
            "  AND qh.user = :user\n" +
            "  AND qh.quiz.stackUnit = :stack\n" +
            "  AND qh.quizStatus = 'SUCCESS'")
    Double findSuccessHistoryByStacks(User user, Stack stack, BaseStatus status);
}
