package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.quiz.Repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.Repository.QuizRepository;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.quiz.model.GetPagedQuizRes;
import com.cs.quizeloper.quiz.model.GetQuizRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    @Transactional
    public GetPagedQuizRes getQuizList(PageRequest pageRequest) {
        Page<Quiz> pagingQuiz = quizRepository.findAllByStatus(ACTIVE, pageRequest);
        Page<Long> quizLikes = quizLikeRepository.findAllByUserAndStatus(1L, ACTIVE, pageRequest);

        int total = pagingQuiz.getTotalPages();
        int page = pageRequest.getPageNumber();
        boolean nextPage = pagingQuiz.hasNext();

        if(total <= page){
            throw new BaseException(PAGE_COUNT_OVER);
        }

        // List 형식의 QuizUnit 값 String 배열에 담기
        List<String> quizString = pagingQuiz.getContent().stream()
                .flatMap(q -> q.getQuizUnitList().stream().map(q2 -> String.valueOf(q2.getQuizUnit().getUnit())))
                .collect(Collectors.toList());

        // 최종적으로 DTO에 반환
        List<GetQuizRes> quizResList = pagingQuiz.getContent().stream()
                .map(quiz -> new GetQuizRes(quiz.getId(), quiz.getTitle(), quiz.getType(), quiz.getStackUnit(), quizString,
                        checkUserLikesQuiz(quizLikes.toList(), quiz)))
                .collect(Collectors.toList());

        return new GetPagedQuizRes(quizResList, total, page, nextPage);
    }

    // 퀴즈 좋아요 확인 여부
    private boolean checkUserLikesQuiz(List<Long> quizLike, Quiz quiz) {
        return quizLike.contains(quiz.getId());
    }
}
