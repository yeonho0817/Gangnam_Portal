package com.gangnam.portal.util.jwt;

import com.gangnam.portal.repository.redis.RedisRepository;
import com.gangnam.portal.util.jwt.redis.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final RedisRepository redisRepository;

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String email, String nameKr) {
        return "Bearer " + doGenerateToken(email, nameKr, JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());
    }

    public String generateRefreshToken(String email, String nameKr) {
        // redis 저장
        String refreshToken = doGenerateToken(email, nameKr, JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue());

        redisRepository.save(new RefreshToken(refreshToken, email, JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue()));

        return "Bearer " + refreshToken;
    }

    private String doGenerateToken(String email, String nameKr, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("nameKr", nameKr);
        claims.put("role", "ROLE_USER");

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

}