package com.cs.quizeloper.user.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SignupReq {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "비밀번호를 숫자, 문자, 특수문자 포함 8~15자리 이내로 입력해주세요"
    )
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(
            regexp = "^[가-힣ㄱ-ㅎa-zA-Z0-9._-]{6,}\\$",
            message = "닉네임을 숫자, 문자를 포함하여 6자리 이상으로 입력해주세요 "
    )
    private String nickname;
}
