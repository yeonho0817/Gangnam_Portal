package com.gangnam.portal.util.jwt.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash(value = "accessToken")
//HGETALL 자료형식
public class AccessToken {
    @Id
    private String accessToken;

    @TimeToLive
    private Long expiration;

    public AccessToken(final String accessToken, final Long expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
