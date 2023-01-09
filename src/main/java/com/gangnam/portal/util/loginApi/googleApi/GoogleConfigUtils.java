package com.gangnam.portal.util.loginApi.googleApi;

import com.gangnam.portal.util.loginApi.ConfigUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleConfigUtils extends ConfigUtils {
    @Value("${spring.security.oauth2.client.registration.google.authUri}")
    private String googleAuthUri;

    @Value("${spring.security.oauth2.client.registration.google.loginUri}")
    private String googleLoginUri;

    @Value("${spring.security.oauth2.client.registration.google.tokenUri}")
    private String googleTokenUri;

    @Value("${spring.security.oauth2.client.registration.google.userInfoUri}")
    private String googleUserInfoUri;

    @Value("${spring.security.oauth2.client.registration.google.redirectUri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String googleSecret;

    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private String googleScopes;

    public GoogleConfigUtils() {
        provider = "google";

        authUri = googleAuthUri;
        loginUri = googleLoginUri;
        tokenUri = googleTokenUri;
        userInfoUri = googleUserInfoUri;
        redirectUri = googleRedirectUri;
        clientId = googleClientId;
        secret = googleSecret;
        scopes = googleScopes;
    }

    @Override
    public String initUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", getClientId());
        params.put("redirect_uri", getRedirectUri());
        params.put("response_type", "code");
        params.put("scope", getScopeUrl());

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return loginUri
                + "?"
                + paramStr;
    }

}