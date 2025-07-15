package com.ptip.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    /**
     * AccessToken 생성 (사용자 식별용 ID 포함)
     */
    public String createAccessToken(String userId) {
        return JWT.create()
                .withSubject("AccessToken")
                .withClaim("userId", userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .sign(Algorithm.HMAC256(secretKey));
    }

    /**
     * RefreshToken 생성 (userId 없음, 서버에서 DB로 매칭)
     */
    public String createRefreshToken() {
        return JWT.create()
                .withSubject("RefreshToken")
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .sign(Algorithm.HMAC256(secretKey));
    }
}
