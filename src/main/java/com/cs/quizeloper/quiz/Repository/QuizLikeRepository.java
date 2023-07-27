package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.quiz.entity.QuizLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizLikeRepository extends JpaRepository<QuizLike, Long> {
}
