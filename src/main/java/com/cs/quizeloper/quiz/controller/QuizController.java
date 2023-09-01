package com.cs.quizeloper.quiz.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.resolver.Account;
import com.cs.quizeloper.global.resolver.UserInfo;
import com.cs.quizeloper.quiz.model.GetQuizRes;
import com.cs.quizeloper.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    /**
     * [GET] 퀴즈 목록 불러오기
     * @return BaseResponse<GetPagedQuizRes>
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<Page<GetQuizRes>> getQuizList(@Account UserInfo userInfo, @RequestParam(name = "size", defaultValue = "16") Integer size,
                                                      @RequestParam(name = "page", defaultValue = "0") Integer page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<GetQuizRes> quizPage = quizService.getQuizList(userInfo.getId(), pageRequest);
        return new BaseResponse<>(quizPage);
    }

    /**
     * [GET] 조건별로 필터링한 퀴즈 목록 불러오기
     * @return BaseResponse<GetQuizRes>
     */
    @ResponseBody
    @GetMapping("/{stack}")
    public BaseResponse<Page<GetQuizRes>> getFilteredQuizList(@RequestParam(name = "size", defaultValue = "16") Integer size, @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                          @Account UserInfo userInfo, @PathVariable String stack,
                                                          @RequestParam(name = "quizType", required = false) String quizType, @RequestParam(name = "range", required = false) Long ... range) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<GetQuizRes> quizPage = quizService.getFilteredQuizList(userInfo.getId(), stack, quizType, pageRequest, range);
        return new BaseResponse<>(quizPage);
    }
}
