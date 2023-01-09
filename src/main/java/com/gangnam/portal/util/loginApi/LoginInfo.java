package com.gangnam.portal.util.loginApi;

import org.springframework.http.ResponseEntity;

public interface LoginInfo {
    ResponseEntity<Object> loginUri();
    UserInfoDto redirectInfo(String authCode);
}
