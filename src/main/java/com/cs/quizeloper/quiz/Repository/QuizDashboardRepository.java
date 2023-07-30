package com.cs.quizeloper.quiz.Repository;

import com.cs.quizeloper.quiz.entity.QuizDashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizDashboardRepository extends JpaRepository<QuizDashboard, Long> {
}
