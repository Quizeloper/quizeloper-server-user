package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.global.resolver.UserInfo;
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
    public Page<GetQuizRes> getQuizList(Long userId, PageRequest pageRequest) {
        Page<Quiz> pagingQuiz = quizRepository.findAllByStatus(ACTIVE, pageRequest);
        Page<Long> quizLikes = quizLikeRepository.findAllByUserAndStatus(userId, ACTIVE, pageRequest);

        List<String> quizString = getQuizUnitList(pagingQuiz.getContent());

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

    // 퀴즈 문제 유형 반환
    private List<String> getQuizUnitList(List<Quiz> quizList){
        return quizList.stream()
                .flatMap(quiz -> quiz.getQuizUnitList().stream().map(QuizUnitList::getQuizUnit))
                .map(quizUnit -> String.valueOf(quizUnit.getUnit()))
                .collect(Collectors.toList());
    }
}
