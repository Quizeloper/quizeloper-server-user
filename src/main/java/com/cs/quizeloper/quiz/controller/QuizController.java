package com.cs.quizeloper.quiz.controller;

import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.quiz.model.GetPagedQuizRes;
import com.cs.quizeloper.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import static com.cs.quizeloper.global.exception.BaseResponseStatus.PAGE_COUNT_UNDER;
import static com.cs.quizeloper.global.exception.BaseResponseStatus.PAGE_SIZE_COUNT_UNDER;

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
    public BaseResponse<GetPagedQuizRes> getQuizList(@RequestParam(name = "size", defaultValue = "16") Integer size,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page) {
        try {
            if (page < 0) {
                return new BaseResponse<>(PAGE_COUNT_UNDER);
            }
            if (size < 1) {
                return new BaseResponse<>(PAGE_SIZE_COUNT_UNDER);
            }
            PageRequest pageRequest = PageRequest.of(page, size);
            return new BaseResponse<>(quizService.getQuizList(pageRequest));
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
