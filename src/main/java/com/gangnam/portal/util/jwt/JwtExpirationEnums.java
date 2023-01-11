package com.gangnam.portal.util.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtExpirationEnums {

    ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 30분", 1000L * 60 * 30),
    REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),

    TEST_ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 30초", 1000L * 30),
    TEST_REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 30초", 1000L * 30),

    // 테스트
    LONG_ACCESS_TOKEN_EXPIRATION_TIME("JWT 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),

    ;

    private String description;
    private Long value;
}