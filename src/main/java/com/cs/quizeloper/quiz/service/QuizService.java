package com.cs.quizeloper.quiz.service;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.quiz.repository.*;
import com.cs.quizeloper.quiz.repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.repository.QuizRepository;
import com.cs.quizeloper.quiz.repository.QuizUnitRepository;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.quiz.model.*;
import com.cs.quizeloper.user.repository.UserRepository;
import com.cs.quizeloper.user.entity.User;
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
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizLikeRepository quizLikeRepository;
    private final QuizUnitRepository quizUnitRepository;
    private final QuizSolvingRepository quizSolvingRepository;
    private final QuizHistoryRepository quizHistoryRepository;

    // 개별 퀴즈 조회
    public GetQuizDetailRes getQuizDetail(Long userId, Long quizId) {
        Quiz quiz = quizRepository.findByIdAndStatus(quizId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.QUIZ_NOT_FOUND));
        List<Long> quizLikes = quizLikeRepository.findAllByUserIdAndStatus(userId, ACTIVE);
        List<GetSolvingRes> solvings = quizSolvingRepository.findAllByQuizIdAndStatus(quiz.getId(), ACTIVE).stream()
                        .map(solver -> new GetSolvingRes(solver.getNumber(), solver.getQuestion()))
                        .toList();
        return GetQuizDetailRes.toDto(quiz, getQuizUnitList(quiz), solvings, checkUserLikesQuiz(quizLikes, quiz));
    }

    // 퀴즈 풀기
    public void postQuizDetail(Long userId, Long quizId, PostQuizReq postQuizReq) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Quiz quiz = quizRepository.findByIdAndStatus(quizId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.QUIZ_NOT_FOUND));
        if (quiz.getAnswer().equals(postQuizReq.getAnswer())) { quizHistoryRepository.save(QuizHistory.toEntity(QuizStatus.SUCCESS, quiz, user));}
        else { quizHistoryRepository.save(QuizHistory.toEntity(QuizStatus.FAILURE, quiz, user)); }
    }

    // 퀴즈 좋아요
    @Transactional
    public void postQuizLikes(Long userId, Long quizId){
        User user = userRepository.findByIdAndStatus(userId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        Quiz quiz = quizRepository.findByIdAndStatus(quizId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.QUIZ_NOT_FOUND));
        if (quizLikeRepository.existsIdByUserIdAndQuizId(user.getId(), quizId)) {
            quizLikeRepository.deleteByUserIdAndQuizId(userId, quizId);
        }
        else {
            QuizLike quizLike = QuizLike.builder().user(user).quiz(quiz).build();
            quizLikeRepository.save(quizLike);
        }

    }

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

    // 퀴즈 문제 유형 조회
    public List<GetQuizUnitRes> getQuizUnitList(Long userId, String stack) {
        userRepository.findByIdAndStatus(userId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        return quizUnitRepository.findAllByStackAndStatus(Stack.valueOf(stack.toUpperCase()), ACTIVE)
                .stream()
                .map(GetQuizUnitRes::toDto)
                .collect(Collectors.toList());
    }

    // 퀴즈 답안 & 해설 조회
    public GetQuizAnsRes getQuizAnswer(long userId, long quizId) {
        QuizHistory quizHistory= quizHistoryRepository.findFirstByQuizIdAndUserIdAndStatusOrderByCreatedDateDesc(quizId, userId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.QUIZ_HISTORY_NOT_FOUND));
        Quiz quiz = quizRepository.findByIdAndStatus(quizId, ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.QUIZ_NOT_FOUND));
        return GetQuizAnsRes.toDto(quiz, quizHistory);
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
