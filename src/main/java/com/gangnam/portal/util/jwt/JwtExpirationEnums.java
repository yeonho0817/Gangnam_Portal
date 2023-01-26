package com.gangnam.portal.util.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtExpirationEnums {

    ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 30분", 1000L * 60 * 30),
    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),

    // 테스트
    TEST_SHORT_ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 10초", 1000L * 10),
    TEST_SHORT_REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 10초", 1000L * 10),
    TEST_LONG_ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),

    ;

    private String description;
    private Long value;
}