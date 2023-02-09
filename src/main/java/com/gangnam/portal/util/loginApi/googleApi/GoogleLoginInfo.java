package com.gangnam.portal.util.loginApi.googleApi;

import com.gangnam.portal.dto.LoginApiDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GoogleLoginInfo {
    @Autowired
    private final RestTemplate restTemplate;

    private final GoogleConfigUtils googleConfigUtils;

    public ResponseEntity<Object> googleLoginUri() {
        String authUrl = googleConfigUtils.googleInitUrl();

        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);

            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getGoogleAccessToken(String authCode ) {
        try {
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(googleConfigUtils.getGoogleTokenUrl())

                    .build();

            LoginApiDTO.GoogleLoginAccessRequest googleLoginAccessRequest = LoginApiDTO.GoogleLoginAccessRequest.builder()
                    .grantType("authorization_code")
                    .clientId(googleConfigUtils.getGoogleClientId())
                    .clientSecret(googleConfigUtils.getGoogleSecret())
                    .redirectUri(googleConfigUtils.getGoogleRedirectUrl())
                    .code(authCode)

                    .build();

            ResponseEntity<LoginApiDTO.GoogleLoginAccessResponse> response = restTemplate.postForEntity(
                    uri.toUriString(),
                    googleLoginAccessRequest,
                    LoginApiDTO.GoogleLoginAccessResponse.class);

            return Objects.requireNonNull(response.getBody()).getIdToken();
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.LOGIN_FAILED);
        }
    }

    public String getGoogleUserInfo(String accessToken) {
        try {
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(googleConfigUtils.getGoogleUserInfoUrl())

                    .build();

            LoginApiDTO.GoogleLoginUserInfoRequest googleLoginUserInfoRequest = LoginApiDTO.GoogleLoginUserInfoRequest.builder()
                    .idToken(accessToken)

                    .build();

            ResponseEntity<LoginApiDTO.GoogleLoginUserInfoResponse> response = restTemplate.postForEntity(
                    uri.toUriString(),
                    googleLoginUserInfoRequest,
                    LoginApiDTO.GoogleLoginUserInfoResponse.class);

            System.out.println(response);
            return Objects.requireNonNull(response.getBody()).getEmail();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorStatus.LOGIN_FAILED);
        }
    }


}