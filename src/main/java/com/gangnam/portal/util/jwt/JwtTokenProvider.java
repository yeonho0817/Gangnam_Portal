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

    public Date getExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long id, String email, String provider) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("email", email);
        claims.put("provider", provider);
        claims.put("role", "ROLE_USER");

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtExpirationEnums./*TEST_SHORT_*/ACCESS_TOKEN_EXPIRATION_TIME.getValue()))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + token;
    }

    // refresh token은 claim을 추가하지 않음
    public String generateRefreshToken() {
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue()))
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
        Date expiration = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }


}