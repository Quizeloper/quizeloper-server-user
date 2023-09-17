package com.cs.quizeloper.user.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.global.resolver.Account;
import com.cs.quizeloper.global.resolver.UserInfo;
import com.cs.quizeloper.user.dto.*;
import com.cs.quizeloper.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원 가입
    @PostMapping("/signup")
    public BaseResponse<TokenDto> signUp(@RequestBody @Valid SignupReq signupReq){
        return new BaseResponse<>(userService.signUp(signupReq));
    }

    // 로그인
    @PostMapping("/login")
    public BaseResponse<TokenDto> login(@RequestBody @Valid LoginReq loginReq){
        return new BaseResponse<>(userService.login(loginReq));
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public BaseResponse<TokenDto> reissue(@RequestBody TokenDto tokenDto){
        return new BaseResponse<>(userService.reissue(tokenDto));
    }

    // 로그아웃
    @PostMapping("/logout")
    public BaseResponse<BaseResponseStatus> logout(@Account UserInfo userInfo, HttpServletRequest request){
        userService.blackListAccessToken(userInfo.getId(), request);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 회원탈퇴
    @DeleteMapping("/signout")
    public BaseResponse<BaseResponseStatus> signout(@Account UserInfo userInfo, HttpServletRequest request){
        userService.blackListAccessToken(userInfo.getId(), request);
        userService.signout(userInfo.getId());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 비멀번호 확인 (정보 수정, 회원탈퇴 등 사용자의 비밀번호가 맞는지 확인할 때 사용)
    @PostMapping("/password")
    public BaseResponse<BaseResponseStatus> isValidPassword(@Account UserInfo userInfo, @RequestBody @Valid IsValidPasswordReq password){
        userService.isMatchedPassword(userInfo.getId(), password.getPassword());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 비밀번호 수정
    @PatchMapping("/password")
    public BaseResponse<BaseResponseStatus> patchPassword(@RequestBody @Valid LoginReq loginReq){
        userService.patchPassword(loginReq);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }


    // 이메일 중복 확인
    @PostMapping("/email/double-check")
    public BaseResponse<BaseResponseStatus> checkDuplicatedEmail(@RequestBody @Valid CheckDuplicatedEmailReq email){
        userService.checkDuplicatedEmail(email.getEmail());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 닉네임 중복 확인
    @PostMapping("/nickname/double-check")
    public BaseResponse<BaseResponseStatus> checkDuplicatedNickname(@RequestBody @Valid CheckDuplicatedNicknameReq nickname){
        userService.checkDuplicatedNickname(nickname.getNickname());
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 회원정보 수정
    @PatchMapping("/mypage")
    public BaseResponse<BaseResponseStatus> patchMypage(@Account UserInfo userInfo, @RequestBody @Valid PatchMyPageReq myPageReq){
        userService.patchMypage(userInfo.getId(), myPageReq);
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    // 마이페이지
    @GetMapping("/mypage")
    public BaseResponse<MyPageRes> getMypage(@Account UserInfo userInfo){
        return new BaseResponse<>(userService.getMypage(userInfo.getId()));
    }

    // 스택별 좋아요 문제 불러오기
    @GetMapping("/myQuizzes/{stack}/likes")
    public BaseResponse<Page<GetUserQuizRes>> getMyQuizListByStack(@Account UserInfo userInfo, @PathVariable String stack, Pageable pageable) {
        Page<GetUserQuizRes> myQuizzes = userService.getQuizListByStack(userInfo.getId(), stack, pageable);
        return new BaseResponse<>(myQuizzes);
    }

    // 좋아요 문제 불러오기
    @GetMapping("/myQuizzes/likes")
    public BaseResponse<Page<GetUserQuizRes>> getMyQuizList(@Account UserInfo userInfo, Pageable pageable) {
        Page<GetUserQuizRes> myQuizzes = userService.getQuizList(userInfo.getId(), pageable);
        return new BaseResponse<>(myQuizzes);
    }

    @GetMapping("/myQuizzes/{stack}/solving/{solStatus}")
    public BaseResponse<Page<GetUserQuizHistoryRes>> getMyQuizListByStack(@Account UserInfo userInfo, @PathVariable(required = false) String stack,
                                                                          @RequestParam(required = false) String sorting,
                                                                          @PathVariable String solStatus, Pageable pageable) {
        Page<GetUserQuizHistoryRes> quizHistory = userService.getQuizHistoryList(userInfo.getId(), stack, sorting, solStatus, pageable);
        return new BaseResponse<>(quizHistory);
    }
}
