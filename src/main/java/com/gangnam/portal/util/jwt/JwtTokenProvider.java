package com.gangnam.portal.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    public Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }
    public String getEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }
    public String getProvider(String token) {
        return extractAllClaims(token).get("provider", String.class);
    }

    public Long getExpiration(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long id, String email, String provider, String roleType) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("provider", provider);
        claims.put("role", roleType);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtExpirationEnums.TEST_SHORT_ACCESS_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + token;
    }

    public String generateRefreshToken(Long id, String email, String provider, String roleType) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("provider", provider);
        claims.put("role", roleType);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtExpirationEnums.TEST_SHORT_REFRESH_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();


        return "Bearer " + refreshToken;
    }

    public String getResolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    // 로그아웃 -> access 만료 시간 - 현재 시간
    public Long getRemainExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Long expiration = extractAllClaims(accessToken).getExpiration().getTime();
        // 현재 시간
        Long now = new Date().getTime();
        return expiration - now;
    }
}