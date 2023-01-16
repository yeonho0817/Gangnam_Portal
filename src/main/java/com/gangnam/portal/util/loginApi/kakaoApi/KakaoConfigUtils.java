package com.gangnam.portal.util.loginApi.kakaoApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Google 로그인 URL 생성 로직
    public String kakaoInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getKakaoClientId());
        params.put("redirect_uri", getKakaoRedirectUrl());
        params.put("response_type", "code");
        params.put("scope", getScopes());

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return getKakaoLoginUrl()
                + "?"
                + paramStr;
    }

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
