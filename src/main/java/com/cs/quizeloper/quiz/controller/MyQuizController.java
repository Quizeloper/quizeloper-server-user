package com.cs.quizeloper.quiz.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.resolver.Account;
import com.cs.quizeloper.global.resolver.UserInfo;
import com.cs.quizeloper.quiz.service.MyQuizService;
import com.cs.quizeloper.user.dto.GetMyQuizSummary;
import com.cs.quizeloper.user.dto.GetUserQuizHistoryRes;
import com.cs.quizeloper.user.dto.GetUserQuizRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/myQuizzes")
@RequiredArgsConstructor
public class MyQuizController {
    private final MyQuizService myQuizService;

    // 스택별 좋아요 문제 불러오기
    @GetMapping("/{stack}/likes")
    public BaseResponse<Page<GetUserQuizRes>> getMyQuizListByStack(@Account UserInfo userInfo, @PathVariable String stack, Pageable pageable) {
        Page<GetUserQuizRes> myQuizzes = myQuizService.getQuizListByStack(userInfo.getId(), stack, pageable);
        return new BaseResponse<>(myQuizzes);
    }

    // 좋아요 문제 불러오기
    @GetMapping("/likes")
    public BaseResponse<Page<GetUserQuizRes>> getMyQuizList(@Account UserInfo userInfo, Pageable pageable) {
        Page<GetUserQuizRes> myQuizzes = myQuizService.getQuizList(userInfo.getId(), pageable);
        return new BaseResponse<>(myQuizzes);
    }

    // 스택별 맞춘/틀린 문제 조회
    @GetMapping("/{stack}/solving/{solStatus}")
    public BaseResponse<Page<GetUserQuizHistoryRes>> getQuizHistoryByStack(@Account UserInfo userInfo, @PathVariable String stack,
                                                                           @RequestParam(required = false) String sorting,
                                                                           @PathVariable String solStatus, Pageable pageable) {
        Page<GetUserQuizHistoryRes> quizHistory = myQuizService.getQuizHistoryListByStack(userInfo.getId(), stack, sorting, solStatus, pageable);
        return new BaseResponse<>(quizHistory);
    }

    // 전체 맞춘/틀린 문제 조회
    @GetMapping("/solving/{solStatus}")
    public BaseResponse<Page<GetUserQuizHistoryRes>> getQuizHistory(@Account UserInfo userInfo, @RequestParam(required = false) String sorting,
                                                                    @PathVariable String solStatus, Pageable pageable) {
        Page<GetUserQuizHistoryRes> quizHistory = myQuizService.getQuizHistoryList(userInfo.getId(), sorting, solStatus, pageable);
        return new BaseResponse<>(quizHistory);
    }

    // 내 퀴즈 요약
    @GetMapping("/{stack}")
    public BaseResponse<GetMyQuizSummary> getMyQuizzesSummary(@Account UserInfo userInfo, @PathVariable String stack, @RequestParam String date) {
        return new BaseResponse<>(myQuizService.getQuizSummary(userInfo.getId(), stack, date));
    }
}
