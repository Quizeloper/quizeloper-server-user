package com.cs.quizeloper.quiz.repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.Quiz;
import com.cs.quizeloper.quiz.entity.QuizLike;
import com.cs.quizeloper.quiz.entity.Stack;
import com.cs.quizeloper.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuizLikeRepository extends JpaRepository<QuizLike, Long> {
    @Query("SELECT e.id FROM QuizLike e")
    List<Long> findAllByUserIdAndStatus(Long userId, BaseStatus status);
    Boolean existsIdByUserIdAndQuizId(Long userId, Long quizId);
    void deleteByUserIdAndQuizId(Long userId, Long quizId);
    @Query("SELECT q.quiz FROM QuizLike q WHERE q.status = :status and q.quiz.stackUnit = :stack and q.user = :user")
    Page<Quiz> findAllByOrderByCreatedDateDesc(User user, Stack stack, BaseStatus status, Pageable pageable);

    @Query("SELECT q.quiz FROM QuizLike q WHERE q.status = :status and q.user = :user")
    Page<Quiz> findAll(User user, BaseStatus status, Pageable pageable);
}
