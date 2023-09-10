package com.cs.quizeloper.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatchMyPageReq {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$",
            message = "비밀번호를 숫자, 문자, 특수문자 포함 8~15자리 이내로 입력해주세요"
    )
    private String password;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{6,}$",
            message = "닉네임을 숫자, 문자를 사용하여 6자리 이상으로 입력해주세요 "
    )
    private String nickname;
    private String imgKey;
}
