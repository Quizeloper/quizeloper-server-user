package com.cs.quizeloper.user.entity;

import com.cs.quizeloper.global.entity.BaseEntity;
import com.cs.quizeloper.user.dto.SignupReq;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
public class User extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String imgKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String email, String password, String nickname, String imgKey, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imgKey = imgKey;
        this.role = role;
    }

    public static User toEntity(SignupReq signupReq, Role role){
        return User.builder()
                .email(signupReq.getEmail())
                .password(signupReq.getPassword())
                .nickname(signupReq.getNickname())
                .role(role)
                .build();
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }
}
