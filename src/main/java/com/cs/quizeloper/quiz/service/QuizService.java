package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.quiz.Repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.Repository.QuizRepository;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.quiz.model.GetQuizRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cs.quizeloper.global.entity.BaseStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;
    private final QuizLikeRepository quizLikeRepository;

    // 퀴즈 전체 목록 조회
    @Transactional
    public Page<GetQuizRes> getQuizList(Long userId, PageRequest pageRequest) {
        Page<Quiz> pagingQuiz = quizRepository.findAllByStatus(ACTIVE, pageRequest);
        List<Long> quizLikes = quizLikeRepository.findAllByUserIdAndStatus(userId, ACTIVE);

        List<GetQuizRes> quizResList = pagingQuiz.getContent().stream()
                .map(quiz -> new GetQuizRes(quiz.getId(), quiz.getTitle(), quiz.getType(), quiz.getStackUnit(), getQuizUnitList(quiz),
                        checkUserLikesQuiz(quizLikes, quiz)))
                .collect(Collectors.toList());
        return new PageImpl<>(quizResList, pageRequest, pagingQuiz.getTotalElements());
    }

    // 조건별 퀴즈 전체 목록 조회
    public Page<GetQuizRes> getFilteredQuizList(Long userId, String stack, String quizType, PageRequest pageRequest, Long ... range) {
        Page<Quiz> pagingFilteredQuiz;
        Optional<QuizType> quizTypeOptional = Optional.ofNullable(quizType).map(q -> QuizType.valueOf(q.toUpperCase()));

        if (range == null || range.length == 0){
            pagingFilteredQuiz = quizRepository.findAllByStackAndQuizTypeIsNull(pageRequest, Stack.valueOf(stack.toUpperCase()), quizTypeOptional.orElse(null));
        }
        else{
            pagingFilteredQuiz = quizRepository.findAllByStackAndQuizUnitIsNullAndQuizTypeIsNull(pageRequest, Stack.valueOf(stack.toUpperCase()), quizTypeOptional.orElse(null), range);
        }
        List<Long> quizLikes = quizLikeRepository.findAllByUserIdAndStatus(userId, ACTIVE);

        List<GetQuizRes> quizResList = pagingFilteredQuiz.getContent().stream()
                .map(quiz -> new GetQuizRes(quiz.getId(), quiz.getTitle(), quiz.getType(), quiz.getStackUnit(), getQuizUnitList(quiz),
                        checkUserLikesQuiz(quizLikes, quiz)))
                .toList();
        return new PageImpl<>(quizResList, pageRequest, pagingFilteredQuiz.getTotalElements());
    }

    // 퀴즈 좋아요 확인 여부
    private boolean checkUserLikesQuiz(List<Long> quizLike, Quiz quiz) {
        return quizLike.contains(quiz.getId());
    }

    // 퀴즈 문제 유형 반환
    private List<String> getQuizUnitList(Quiz quiz){
        return quiz.getQuizUnitList().stream()
                .map(quizUnitList -> quizUnitList.getQuizUnit().getUnit())
                .toList();
    }
}
