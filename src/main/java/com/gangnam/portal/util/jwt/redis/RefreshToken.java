package com.gangnam.portal.util.jwt.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "refreshToken")  // 7일
//@RedisHash(value = "refreshToken", timeToLive = 10)  // 테스트용 10초
//HGETALL 자료형식
public class RefreshToken {
    @Id
    private String refreshToken;
    private String email;

    @TimeToLive
    private Long expiration;

    public RefreshToken(final String refreshToken, final String email, final Long expiration) {
        this.refreshToken = refreshToken;
        this.email = email;
        this.expiration = expiration;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getEmail() {
        return email;
    }
}
