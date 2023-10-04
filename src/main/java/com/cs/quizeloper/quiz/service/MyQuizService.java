package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.quiz.repository.QuizHistoryRepository;
import com.cs.quizeloper.quiz.repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.repository.QuizRepository;
import com.cs.quizeloper.quiz.entity.Quiz;
import com.cs.quizeloper.quiz.entity.QuizStatus;
import com.cs.quizeloper.quiz.entity.Stack;
import com.cs.quizeloper.user.Repository.UserRepository;
import com.cs.quizeloper.user.dto.GetMyQuizSummary;
import com.cs.quizeloper.user.dto.GetUserQuizHistoryRes;
import com.cs.quizeloper.user.dto.GetUserQuizRes;
import com.cs.quizeloper.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cs.quizeloper.global.entity.BaseStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class MyQuizService {
    private final UserRepository userRepository;
    private final QuizLikeRepository quizLikeRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private final QuizRepository quizRepository;
    public Page<GetUserQuizRes> getQuizListByStack(Long userId, String stack, Pageable pageable){
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        return quizLikeRepository.findAllByOrderByCreatedDateDesc(user, Stack.valueOf(stack.toUpperCase()), ACTIVE, pageable)
                .map(quiz -> GetUserQuizRes.toDto(quiz, getQuizUnitList(quiz)));
    }

    public Page<GetUserQuizRes> getQuizList(Long userId, Pageable pageable){
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        return quizLikeRepository.findAll(user, ACTIVE, pageable)
                .map(quiz -> GetUserQuizRes.toDto(quiz, getQuizUnitList(quiz)));
    }

    public Page<GetUserQuizHistoryRes> getQuizHistoryListByStack(Long userId, String stack, String sorting, String solStatus, Pageable pageable){
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        List<Long> quizLikes = quizLikeRepository.findAllByUserIdAndStatus(userId, ACTIVE);
        Page<Quiz> quizzes = null;
        if (sorting == null) { quizzes = quizHistoryRepository.findAll(user, Stack.valueOf(stack.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("latest")){ quizzes = quizHistoryRepository.findAllByOrderByCreatedDateDesc(user, Stack.valueOf(stack.toUpperCase()), QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("past")) { quizzes = quizHistoryRepository.findAllByOrderByCreatedDateAsc(user, Stack.valueOf(stack.toUpperCase()), QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("large")) { quizzes = quizHistoryRepository.findAllByLarge(user, Stack.valueOf(stack.toUpperCase()), QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("small")) { quizzes = quizHistoryRepository.findAllBySmall(user, Stack.valueOf(stack.toUpperCase()), QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        return quizzes.map(quiz -> GetUserQuizHistoryRes.toDto(quiz, getQuizUnitList(quiz), checkUserLikesQuiz(quizLikes, quiz)));
    }

    public Page<GetUserQuizHistoryRes> getQuizHistoryList(Long userId, String sorting, String solStatus, Pageable pageable){
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        List<Long> quizLikes = quizLikeRepository.findAllByUserIdAndStatus(userId, ACTIVE);
        Page<Quiz> quizzes = null;
        if (sorting == null) { quizzes = quizHistoryRepository.findAll(user, null, ACTIVE, pageable); }
        else if (sorting.equals("latest")){ quizzes = quizHistoryRepository.findAllByOrderByCreatedDateDesc(user, null, QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("past")) { quizzes = quizHistoryRepository.findAllByOrderByCreatedDateAsc(user, null, QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("large")) { quizzes = quizHistoryRepository.findAllByLarge(user, null, QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        else if (sorting.equals("small")) { quizzes = quizHistoryRepository.findAllBySmall(user, null, QuizStatus.valueOf(solStatus.toUpperCase()), ACTIVE, pageable); }
        return quizzes.map(quiz -> GetUserQuizHistoryRes.toDto(quiz, getQuizUnitList(quiz), checkUserLikesQuiz(quizLikes, quiz)));
    }

    public GetMyQuizSummary getQuizSummary (Long userId, String stack, String date){
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Double quizTotalNum = quizRepository.findAllStacks(Stack.valueOf(stack.toUpperCase()), ACTIVE); // 스택별 전체 문제 수
        Double solveNumByStack = quizHistoryRepository.findSolveHistoryByStacks(user, Stack.valueOf(stack.toUpperCase()), ACTIVE); // 스택별 푼 문제 수
        Double successNumByStack = quizHistoryRepository.findSuccessHistoryByStacks(user, Stack.valueOf(stack.toUpperCase()), ACTIVE);// 맞춘 문제 수
        Integer totalNum = quizHistoryRepository.findAllHistoryNum(user, date, ACTIVE); // 시도 문제
        Integer solvedNum = quizHistoryRepository.findSolvedHistoryNum(user, date, ACTIVE);// 푼 문제
        Integer successNum = quizHistoryRepository.findSuccessHistory(user, date, ACTIVE);// 맞은 문제
        Integer failureNum = quizHistoryRepository.findFailureHistory(user, date, ACTIVE);// 틀린 문제
        List<Double> result = new ArrayList<>();
        result.add((successNumByStack / quizTotalNum) * 100); // 스택별 진척도 : 맞춘 문제 수 / 전체 문제 수 (중복 제거)
        result.add((successNumByStack / solveNumByStack) * 100); // 스택별 정답률 : 맞춘 문제 수 / 푼 문제 수 (중복 제거)
        return GetMyQuizSummary.toDto(result, totalNum, solvedNum, successNum, failureNum);
    }

    private List<String> getQuizUnitList(Quiz quiz){
        return quiz.getQuizUnitList().stream()
                .map(quizUnitList -> quizUnitList.getQuizUnit().getUnit())
                .toList();
    }

    private boolean checkUserLikesQuiz(List<Long> quizLike, Quiz quiz) {
        return quizLike.contains(quiz.getId());
    }
}
