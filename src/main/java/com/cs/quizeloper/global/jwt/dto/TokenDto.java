package com.cs.quizeloper.global.jwt.dto;

import com.cs.quizeloper.user.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Role role;

    public static TokenDto toDto(String accessToken, String refreshToken, Role role){
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }
}
