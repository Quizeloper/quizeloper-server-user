package com.cs.quizeloper.global.jwt;

import com.cs.quizeloper.global.constant.Constant;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.user.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static com.cs.quizeloper.global.constant.Constant.Jwt.BEARER_PREFIX;
import static com.cs.quizeloper.global.constant.Constant.Jwt.CLAIM_NAME;


@Component
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    public JwtService(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // token 생성
    public TokenDto createToken(Long userIdx, Role role){
        long now = new Date().getTime();
        String accessToken = Jwts.builder()
                .claim(CLAIM_NAME, userIdx)
                .setSubject(userIdx.toString())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // todo redis refresh token 적용

        return TokenDto.toDto(BEARER_PREFIX + accessToken, BEARER_PREFIX + refreshToken, role);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
        } catch (MalformedJwtException e){
            throw new BaseException(BaseResponseStatus.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new BaseException(BaseResponseStatus.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new BaseException(BaseResponseStatus.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new BaseException(BaseResponseStatus.NULL_TOKEN);
        }
    }

    // 토큰 정보 불러오기
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getUserIdFromJWT(String accessToken){
        String userId = (String) parseClaims(accessToken).get(Constant.Jwt.CLAIM_NAME);
        return Long.parseLong(userId);
    }
}
