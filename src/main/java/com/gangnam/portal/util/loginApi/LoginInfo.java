package com.gangnam.portal.util.loginApi;

import org.springframework.http.ResponseEntity;

public interface LoginInfo {
    String getInitUrl();
    ResponseEntity<Object> getLoginUri();
    String getAccessToken(String authCode);
    String getUserInfo(String accessToken);
}
