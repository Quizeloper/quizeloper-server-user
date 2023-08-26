package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.quiz.Repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.Repository.QuizRepository;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.quiz.model.GetPagedQuizRes;
import com.cs.quizeloper.quiz.model.GetQuizRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.cs.quizeloper.global.entity.BaseStatus.ACTIVE;
import static com.cs.quizeloper.global.exception.BaseResponseStatus.PAGE_COUNT_OVER;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizLikeRepository quizLikeRepository;

    // 퀴즈 전체 목록 조회
    public GetPagedQuizRes getQuizList(int size, int page) {
        List<Quiz> quizList= quizRepository.findAllByStatus(ACTIVE);
        List<Long> quizLikes = quizLikeRepository.findAllByUserAndStatus(1L, ACTIVE);

        if(page > quizList.size()/size){
            throw new BaseException(PAGE_COUNT_OVER);
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), quizList.size());

        Page<Quiz> pagingQuiz = new PageImpl<>(quizList.subList(start, end), pageRequest, quizList.size());

        int total = quizList.size();

        List<GetQuizRes> quizResList = pagingQuiz.getContent().stream()
                .map(quiz -> new GetQuizRes(quiz.getTitle(), quiz.getType(), quiz.getStackUnit(), quiz.getQuizUnit().getId(),
                        checkUserLikesQuiz(quizLikes, quiz)))
                .collect(Collectors.toList());

        return new GetPagedQuizRes(quizResList, total, page);
    }

    // 퀴즈 좋아요 확인 여부
    private boolean checkUserLikesQuiz(List<Long> quizLike, Quiz quiz) {
        return quizLike.contains(quiz.getId());
    }
}
