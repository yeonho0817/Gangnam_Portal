package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public abstract class LoginApiDTO {

    // 구글

    @Data
    @Builder
    public static class GoogleLoginAccessRequest {
        @JsonProperty("grant_type")
        private String grantType;
        @JsonProperty("client_id")
        private String clientId;
        @JsonProperty("client_secret")
        private String clientSecret;
        @JsonProperty("redirect_uri")
        private String redirectUri;
        @JsonProperty("code")
        private String code;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleLoginAccessResponse {
        @JsonProperty("id_token")
        private String idToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("scope")
        private String scope;
        @JsonProperty("expires_in")
        private int expiresIn;
        @JsonProperty("access_token")
        private String accessToken;
    }

    @Data
    @Builder
    public static class GoogleLoginUserInfoRequest {
        @JsonProperty("id_token")
        private String idToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoogleLoginUserInfoResponse {
        @JsonProperty("email")
        private String email;
    }

}
