package com.gangnam.portal.util.loginApi;

import lombok.Data;

@Data
public abstract class ConfigUtils {
    public String provider;

    public String authUri;
    public String loginUri;
    public String tokenUri;
    public String userInfoUri;
    public String redirectUri;
    public String clientId;
    public String secret;
    public String scopes;

    public abstract String initUrl();

    public String getAuthUri() {
        return authUri;
    }

    public String getLoginUri() {
        return loginUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getScopeUrl() {
        return scopes.replaceAll(",", "%20");
    }
}
