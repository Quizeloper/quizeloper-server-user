package com.cs.quizeloper.global.resolver;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
public class UserInfo {
    private Long id;

    public static UserInfo toDto(Long id){
        return UserInfo.builder().id(id).build();
    }
}
