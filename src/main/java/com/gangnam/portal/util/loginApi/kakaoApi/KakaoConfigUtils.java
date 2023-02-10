package com.gangnam.portal.util.loginApi.kakaoApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoConfigUtils {

    @Value("${spring.security.oauth2.client.provider.kakao.tokenUri}")
    private String kakaoTokenUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.loginUri}")
    private String kakaoLoginUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.redirectUri}")
    private String kakaoRedirectUrl;

    @Value("${spring.security.oauth2.client.provider.kakao.userInfoUri}")
    private String kakaoUserInfoUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.clientId}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.clientSecret}")
    private String kakaoSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.scope}")
    private String scopes;

    public String getKakaoUserInfoUrl() {
        return kakaoUserInfoUrl;
    }

    public String getKakaoLoginUrl() {
        return kakaoLoginUrl;
    }

    public String getKakaoRedirectUrl() {
        return kakaoRedirectUrl;
    }

    public String getKakaoTokenUrl() {
        return kakaoTokenUrl;
    }

    public String getKakaoClientId() {
        return kakaoClientId;
    }

    public String getKakaoSecret() {
        return kakaoSecret;
    }

    public String getScopes() {
        return scopes;
    }
}
