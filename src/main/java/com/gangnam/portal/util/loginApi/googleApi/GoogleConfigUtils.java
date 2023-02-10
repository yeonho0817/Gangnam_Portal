package com.gangnam.portal.util.loginApi.googleApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String getGoogleTokenUrl() {
        return googleTokenUrl;
    }

    public String getGoogleLoginUrl() { return googleLoginUrl; }

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
