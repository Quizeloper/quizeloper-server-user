package com.cs.quizeloper.quiz.repository;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.quiz.entity.QuizDashboard;
import com.cs.quizeloper.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizDashboardRepository extends JpaRepository<QuizDashboard, Long> {
    List<QuizDashboard> findByUserAndStatus(User user, BaseStatus status);
}
