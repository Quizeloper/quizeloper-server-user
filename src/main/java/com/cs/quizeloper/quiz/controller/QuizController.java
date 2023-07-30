package com.cs.quizeloper.quiz.controller;

import com.cs.quizeloper.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;
}
