package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.quiz.Repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.Repository.QuizRepository;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.quiz.model.GetQuizRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.cs.quizeloper.global.entity.BaseStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizLikeRepository quizLikeRepository;

    // 퀴즈 전체 목록 조회
    @Transactional
    public Page<GetQuizRes> getQuizList(PageRequest pageRequest) {
        Page<Quiz> pagingQuiz = quizRepository.findAllByStatus(ACTIVE, pageRequest);
        Page<Long> quizLikes = quizLikeRepository.findAllByUserAndStatus(1L, ACTIVE, pageRequest);

        // List 형식의 QuizUnit 값 String 배열에 담기
        List<String> quizString = pagingQuiz.getContent().stream()
                .flatMap(quiz -> quiz.getQuizUnitList().stream().map(QuizUnitList::getQuizUnit))
                .map(quizUnit -> String.valueOf(quizUnit.getUnit()))
                .collect(Collectors.toList());

        // 최종적으로 DTO에 반환
        List<GetQuizRes> quizResList = pagingQuiz.getContent().stream()
                .map(quiz -> new GetQuizRes(quiz.getId(), quiz.getTitle(), quiz.getType(), quiz.getStackUnit(), quizString,
                        checkUserLikesQuiz(quizLikes.toList(), quiz)))
                .collect(Collectors.toList());

        return new PageImpl<>(quizResList, pageRequest, pagingQuiz.getTotalElements());
    }

    // 퀴즈 좋아요 확인 여부
    private boolean checkUserLikesQuiz(List<Long> quizLike, Quiz quiz) {
        return quizLike.contains(quiz.getId());
    }
}
