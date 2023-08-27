package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.QuizLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface QuizLikeRepository extends JpaRepository<QuizLike, Long> {
    @Query("SELECT e.id FROM QuizLike e")
    Page<Long> findAllByUserAndStatus(Long userId, BaseStatus status, Pageable pageable);
}
