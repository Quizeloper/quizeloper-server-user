package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.QuizUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizUnitRepository extends JpaRepository<QuizUnit, Long> {
    List<QuizUnit> findAllByStatus(BaseStatus status);
}
