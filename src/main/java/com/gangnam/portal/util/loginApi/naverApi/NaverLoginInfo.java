package com.gangnam.portal.util.loginApi.naverApi;

import com.gangnam.portal.util.loginApi.LoginInfo;
import com.gangnam.portal.util.loginApi.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NaverLoginInfo implements LoginInfo {
    private final NaverConfigUtils naverConfigUtils;
    @Override
    public ResponseEntity<Object> loginUri() {
        return null;
    }

    @Override
    public UserInfoDto redirectInfo(String authCode) {
        return null;
    }
}
