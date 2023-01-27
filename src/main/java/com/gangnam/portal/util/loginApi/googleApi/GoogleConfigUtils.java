package com.gangnam.portal.util.loginApi.googleApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleConfigUtils {
    @Value("${spring.security.oauth2.client.registration.google.tokenUri}")
    private String googleTokenUrl;

    @Value("${spring.security.oauth2.client.registration.google.loginUri}")
    private String googleLoginUrl;

    @Value("${spring.security.oauth2.client.registration.google.redirectUri}")
    private String googleRedirectUrl;

    @Value("${spring.security.oauth2.client.registration.google.userInfoUri}")
    private String googleUserInfoUrl;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String googleSecret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String scopes;

    // Google 로그인 URL 생성 로직
    public String googleInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getGoogleClientId());
        params.put("redirect_uri", getGoogleRedirectUrl());
        params.put("response_type", "code");
        params.put("scope", getScopeUrl());
        params.put("prompt", "select_account");

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return getGoogleLoginUrl()
                + "?"
                + paramStr;
    }

    public String getGoogleTokenUrl() {
        return googleTokenUrl;
    }

    public String getGoogleLoginUrl() {
        return googleLoginUrl;
    }

    public String getGoogleRedirectUrl() {
        return googleRedirectUrl;
    }

    public String getGoogleUserInfoUrl() {
        return googleUserInfoUrl;
    }

    public String getGoogleClientId() {
        return googleClientId;
    }

    public String getGoogleSecret() {
        return googleSecret;
    }

    // scope의 값을 보내기 위해 띄어쓰기 값을 UTF-8로 변환하는 로직 포함
    public String getScopeUrl() {
        return scopes.replaceAll(",", "%20");
    }
}
