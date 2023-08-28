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
    @Size(
            message = "닉네임을 두자리 이상 입력해주세요.",
            min = 2
    )
    private String nickname;
}
