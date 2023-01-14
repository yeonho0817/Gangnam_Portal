package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class AuthDTO {

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class LoginUriDTO {
        private String uriPath;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class TokenDTO {
        private String accessToken;
        private String refreshToken;
        private String role;
    }


}
