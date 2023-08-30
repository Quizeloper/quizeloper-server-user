package com.cs.quizeloper.user.service;

import com.cs.quizeloper.global.constant.Constant;
import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.JwtService;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.user.Repository.UserRepository;
import com.cs.quizeloper.user.dto.LoginReq;
import com.cs.quizeloper.user.dto.SignupReq;
import com.cs.quizeloper.user.entity.Role;
import com.cs.quizeloper.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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
}
