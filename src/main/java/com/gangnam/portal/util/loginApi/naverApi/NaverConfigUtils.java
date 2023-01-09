package com.gangnam.portal.util.loginApi.naverApi;

import com.gangnam.portal.util.loginApi.ConfigUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class NaverConfigUtils extends ConfigUtils {
    @Value("${spring.security.oauth2.client.provider.naver.authUri}")
    private String naverAuthUri;

    @Value("${spring.security.oauth2.client.provider.naver.loginUri}")
    private String naverLoginUri;

    @Value("${spring.security.oauth2.client.provider.naver.tokenUri}")
    private String naverTokenUri;

    @Value("${spring.security.oauth2.client.provider.naver.userInfoUri}")
    private String naverUserInfoUri;

    @Value("${spring.security.oauth2.client.provider.naver.redirectUri}")
    private String naverRedirectUri;

    @Value("${spring.security.oauth2.client.registration.naver.clientId}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.clientSecret}")
    private String naverSecret;

    @Value("${spring.security.oauth2.client.registration.naver.scope}")
    private String naverScopes;

    public NaverConfigUtils() {
        provider = "naver";

        authUri = naverAuthUri;
        loginUri = naverLoginUri;
        tokenUri = naverTokenUri;
        userInfoUri = naverUserInfoUri;
        redirectUri = naverRedirectUri;
        clientId = naverClientId;
        secret = naverSecret;
        scopes = naverScopes;
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
