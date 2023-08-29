package com.cs.quizeloper.user.controller;

import com.cs.quizeloper.global.exception.BaseResponse;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.user.dto.LoginReq;
import com.cs.quizeloper.user.dto.SignupReq;
import com.cs.quizeloper.user.service.UserService;
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
}
