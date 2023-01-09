package com.gangnam.portal.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Component
public class JwtTokenUtil {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;
//    @Value("${spring.jwt.token-validity-in-seconds}")
//    private long tokenValidityInMilliseconds;

    private Key key;

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        return extractAllClaims(token).get("userId", String.class);
    }

    public Long getMemberId(String token) {
        return extractAllClaims(token).get("memberId", Long.class);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String resolveToken(String token)
    {
        return token.substring(7);
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateAccessToken(Long memberId, String userId) {
        return doGenerateToken(memberId, userId, JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME.getValue());
    }

    public String generateRefreshToken(Long memberId, String userId) {
        return doGenerateToken(memberId, userId, JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue());
    }

    private String doGenerateToken(Long memberId, String userId, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
       String userId = getUserId(token);

        return userId.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public long getRemainMilliSeconds(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }


}