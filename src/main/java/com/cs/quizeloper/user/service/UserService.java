package com.cs.quizeloper.user.service;

import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.JwtService;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.user.Repository.UserRepository;
import com.cs.quizeloper.user.dto.SignupReq;
import com.cs.quizeloper.user.entity.Role;
import com.cs.quizeloper.user.entity.User;
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

}
