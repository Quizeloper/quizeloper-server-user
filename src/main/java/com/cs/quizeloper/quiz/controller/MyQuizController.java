package com.cs.quizeloper.quiz.controller;

import com.cs.quizeloper.quiz.service.MyQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/my-quizzes")
@RequiredArgsConstructor
public class MyQuizController {
    private final MyQuizService myQuizService;
}
