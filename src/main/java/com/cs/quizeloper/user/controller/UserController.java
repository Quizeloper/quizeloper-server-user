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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // 비멀번호 확인 (정보 수정, 회원탈퇴 등 사용자의 비밀번호가 맞는지 확인할 때 사용)
    @PostMapping("/password")
    public BaseResponse<BaseResponseStatus> isValidPassword(@Account UserInfo userInfo, @RequestBody @Valid IsValidPasswordReq password){
        userService.isMatchedPassword(userInfo.getId(), password.getPassword());
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
}
