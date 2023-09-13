package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.QuizLike;
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
}
