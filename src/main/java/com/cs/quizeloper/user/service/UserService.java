package com.cs.quizeloper.user.service;

import com.cs.quizeloper.global.constant.Constant;
import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.JwtService;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.inquiry.repository.InquiryRepository;
import com.cs.quizeloper.inquiry.entity.InquiryStatus;
import com.cs.quizeloper.quiz.repository.QuizDashboardRepository;
import com.cs.quizeloper.quiz.repository.QuizHistoryRepository;
import com.cs.quizeloper.quiz.repository.QuizLikeRepository;
import com.cs.quizeloper.quiz.repository.QuizRepository;
import com.cs.quizeloper.quiz.entity.*;
import com.cs.quizeloper.user.repository.UserRepository;
import com.cs.quizeloper.user.dto.*;
import com.cs.quizeloper.user.entity.Role;
import com.cs.quizeloper.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cs.quizeloper.global.entity.BaseStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final QuizDashboardRepository quizDashboardRepository;
    private final InquiryRepository inquiryRepository;
    private final QuizLikeRepository quizLikeRepository;
    private final QuizHistoryRepository quizHistoryRepository;
    private final QuizRepository quizRepository;

    public TokenDto signUp(SignupReq signupReq) {
        if (userRepository.existsByEmailAndStatus(signupReq.getEmail(), BaseStatus.ACTIVE)) throw new BaseException(BaseResponseStatus.DUPLICATE_USER_EMAIL);
        if (userRepository.existsByNicknameAndStatus(signupReq.getNickname(), BaseStatus.ACTIVE)) throw new BaseException(BaseResponseStatus.DUPLICATE_USER_NICKNAME);

        String password = signupReq.getPassword();
        signupReq.setPassword(passwordEncoder.encode(password));
        User user = User.toEntity(signupReq, Role.USR);
        userRepository.save(user);

        return jwtService.createToken(user.getId(), user.getRole());
    }

    public TokenDto login(LoginReq loginReq) {
        User user = userRepository.findByEmailAndStatus(loginReq.getEmail(), BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        isMatchedPassword(loginReq.getPassword(), user.getPassword());
        return jwtService.createToken(user.getId(), user.getRole());
    }

    private void isMatchedPassword(String password, String userPassword) {
        if(!passwordEncoder.matches(password, userPassword)) throw new BaseException(BaseResponseStatus.INVALID_USER_PASSWORD);
    }

    public TokenDto reissue(TokenDto tokenDto) {
        // delete bearer context
        tokenDto.toReplaceDto();
        // validate refresh token
        jwtService.validateToken(tokenDto.getRefreshToken());
        // get user info
        Long userId = jwtService.getUserIdFromJWT(tokenDto.getAccessToken());
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        // validate refresh token
        jwtService.validateRefreshToken(userId, tokenDto.getRefreshToken());
        jwtService.deleteRefreshToken(userId);

        return jwtService.createToken(userId, user.getRole());
    }

    public void blackListAccessToken(Long userId, HttpServletRequest request) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        // header 에서 토큰 불러오기
        String bearerToken = request.getHeader(Constant.Jwt.AUTHORIZATION_HEADER);
        // accessToken blackList 처리
        jwtService.blackListToken(bearerToken, BaseStatus.LOGOUT);
        // refreshToken 삭제
        jwtService.deleteRefreshToken(userId);
    }

    public void isMatchedPassword(Long userId, String password) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        isMatchedPassword(password, user.getPassword());
    }

    public void checkDuplicatedEmail(String email) {
        if (userRepository.existsByEmailAndStatus(email, BaseStatus.ACTIVE)) throw new BaseException(BaseResponseStatus.DUPLICATE_USER_EMAIL);

    }

    public void checkDuplicatedNickname(String nickname) {
        if (userRepository.existsByNicknameAndStatus(nickname, BaseStatus.ACTIVE)) throw new BaseException(BaseResponseStatus.DUPLICATE_USER_NICKNAME);


    }

    public void patchMypage(Long userId, PatchMyPageReq myPageReq) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        // 자기 자신 이외의 닉네임을 확인하여 동일한 닉네임이 존재하는 경우
        if(userRepository.existsByNicknameAndStatusAndIdNot(myPageReq.getNickname(), BaseStatus.ACTIVE, userId)) throw new BaseException(BaseResponseStatus.DUPLICATE_USER_NICKNAME);
        // 비밀번호 수정
        if(!passwordEncoder.matches(user.getPassword(), myPageReq.getPassword())) {
            String password = myPageReq.getPassword();
            user.setPassword(passwordEncoder.encode(password));
        }
        // 닉네임 수정
        if(!user.getNickname().equals(myPageReq.getNickname())) user.setNickname(myPageReq.getNickname());
        // 이미지키가 다른 경우
        if(!user.getImgKey().equals(myPageReq.getImgKey())) user.setImgKey(myPageReq.getImgKey());
        // 수정 저장
        userRepository.save(user);
    }

    public void patchPassword(LoginReq loginReq) {
        // 사용자 불러오기
        User user = userRepository.findByEmailAndStatus(loginReq.getEmail(), BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        // 비밀번호 수정
        String password = loginReq.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        // 수정 저장
        userRepository.save(user);
    }

    public MyPageRes getMypage(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        List<QuizDashboard> dashboards = quizDashboardRepository.findByUserAndStatus(user, BaseStatus.ACTIVE);
        MyPageQuestionRes myPageQuestionRes = MyPageQuestionRes.toDto(
                inquiryRepository.countByUserAndStatus(user, BaseStatus.ACTIVE),
                inquiryRepository.countByUserAndStatusAndInquiryStatus(user, BaseStatus.ACTIVE, InquiryStatus.REPLIED)
        );
        return MyPageRes.toDto(user.getEmail(), user.getNickname(), dashboards, myPageQuestionRes);
    }

    public void signout(Long userId) {
        User user = userRepository.findByIdAndStatus(userId, BaseStatus.ACTIVE).orElseThrow(() -> new BaseException(BaseResponseStatus.USER_NOT_FOUND));
        // todo: cascade 적용후 테스트 진행
        userRepository.delete(user);
    }

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
