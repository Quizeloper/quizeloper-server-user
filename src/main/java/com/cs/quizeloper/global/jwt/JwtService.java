package com.cs.quizeloper.global.jwt;

import com.cs.quizeloper.global.constant.Constant;
import com.cs.quizeloper.global.entity.BaseStatus;
import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.dto.TokenDto;
import com.cs.quizeloper.global.service.RedisService;
import com.cs.quizeloper.user.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.cs.quizeloper.global.constant.Constant.Jwt.BEARER_PREFIX;
import static com.cs.quizeloper.global.constant.Constant.Jwt.CLAIM_NAME;


@Component
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    private final RedisService redisService;

    public JwtService(@Value("${jwt.secret}") String secretKey, RedisService redisService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisService = redisService;
    }

    // token 생성
    public TokenDto createToken(Long userIdx, Role role){
        long now = new Date().getTime();
        String accessToken = Jwts.builder()
                .claim(CLAIM_NAME, userIdx.toString())
                .setSubject(userIdx.toString())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        redisService.setValue(userIdx.toString(), refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));
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

    // 사용자 정보 불러오기
    public Long getUserIdFromJWT(String accessToken){
        String userId = (String) parseClaims(accessToken).get(Constant.Jwt.CLAIM_NAME);
        return Long.parseLong(userId);
    }

    // 토큰 유효성 검사
    public void validateRefreshToken(Long userId, String refreshToken){
        String redisToken = redisService.getValue(userId.toString());
        if(redisToken == null || !redisToken.equals(refreshToken)) throw new BaseException(BaseResponseStatus.INVALID_TOKEN);
    }

    // refreshToken 삭제
    public void deleteRefreshToken(Long userId) {
        if(redisService.getValue(userId.toString()) != null) redisService.deleteValue(userId.toString());
    }

    // blackList 토큰
    public void blackListToken(String token, BaseStatus status) {
        token = token.replace(Constant.Jwt.BEARER_PREFIX, "");
        Long expiration = getExpiration(token);
        // blacklist 처리
        redisService.setValue(token, status.name(), expiration, TimeUnit.MILLISECONDS);
    }

    // token 시간 불러오기
    public Long getExpiration(String token) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}
