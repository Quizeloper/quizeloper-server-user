package com.cs.quizeloper.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckDuplicatedNicknameReq {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{6,}$",
            message = "닉네임을 숫자, 문자를 사용하여 6자리 이상으로 입력해주세요 "
    )
    private String nickname;
}
