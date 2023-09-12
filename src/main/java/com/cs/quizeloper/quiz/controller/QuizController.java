package com.cs.quizeloper.quiz.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.resolver.Account;
import com.cs.quizeloper.global.resolver.UserInfo;
import com.cs.quizeloper.quiz.model.GetQuizDetailRes;
import com.cs.quizeloper.quiz.model.GetQuizRes;
import com.cs.quizeloper.quiz.model.PostQuizReq;
import com.cs.quizeloper.quiz.model.GetQuizUnitRes;
import com.cs.quizeloper.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
public class QuizController {
    private final QuizService quizService;

    /**
     * [GET] 퀴즈 보기
     * @return BaseResponse<GetQuizRes>
     */
    @ResponseBody
    @GetMapping("/quiz/{quizIdx}")
    public BaseResponse<GetQuizDetailRes> getQuizDetail(@Account UserInfo userInfo, @PathVariable long quizIdx) {
        GetQuizDetailRes quizDetail = quizService.getQuizDetail(userInfo.getId(), quizIdx);
        return new BaseResponse<>(quizDetail);
    }

    /**
     * [POST] 퀴즈 풀기
     * @return BaseResponse<BaseResponseStatus>
     */
    @ResponseBody
    @PostMapping("/quiz/{quizIdx}")
    public BaseResponse<BaseResponseStatus> postQuizDetail(@Account UserInfo userInfo, @PathVariable long quizIdx, @RequestBody PostQuizReq postQuizReq) {
        quizService.postQuizDetail(userInfo.getId(), quizIdx, postQuizReq);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    /**
     * [GET] 퀴즈 목록 불러오기
     * @return BaseResponse<Page<GetQuizRes>
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
     * @return BaseResponse<Page<GetQuizRes>>
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

    /**
     * [GET] 퀴즈 문제 유형 목록 불러오기
     * @return BaseResponse<List<GetQuizUnitRes>>
     */
    @ResponseBody
    @GetMapping("/quizUnit")
    public BaseResponse<List<GetQuizUnitRes>> getQuizUnitList(@Account UserInfo userInfo) {
        return new BaseResponse<>(quizService.getQuizUnitList());
    }
}
