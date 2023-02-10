package com.gangnam.portal.util.loginApi.kakaoApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.util.loginApi.LoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KaKaoLoginInfo implements LoginInfo {
    @Autowired
    private final RestTemplate restTemplate;

    private final KakaoConfigUtils kakaoConfigUtils;

    @Override
    public String getInitUrl() {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", kakaoConfigUtils.getKakaoClientId());
        params.put("redirect_uri", kakaoConfigUtils.getKakaoRedirectUrl());
        params.put("response_type", "code");
        params.put("scope", kakaoConfigUtils.getScopes());
        params.put("prompt", "login");

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        return kakaoConfigUtils.getKakaoLoginUrl()
                + "?"
                + paramStr;
    }
    @Override
    public ResponseEntity<Object> getLoginUri() {

        String authUrl = getInitUrl();

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

    @Override
    public String getAccessToken(String authCode) {
        try {
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(kakaoConfigUtils.getKakaoTokenUrl())

                    .build();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers  = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoConfigUtils.getKakaoClientId());
            params.add("redirect_uri", kakaoConfigUtils.getKakaoRedirectUrl());
            params.add("code", authCode);

            HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    uri.toUriString(),
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class);

            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.LOGIN_FAILED);
        }
    }

    @Override
    public String getUserInfo(String accessToken) {
        try {
            UriComponents uri = UriComponentsBuilder
                    .fromHttpUrl(kakaoConfigUtils.getKakaoUserInfoUrl())

                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    uri.toUriString(),
                    kakaoUserInfoRequest,
                    String.class);

            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            return jsonNode.get("kakao_account").get("email").asText();
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.LOGIN_FAILED);
        }
    }


}
