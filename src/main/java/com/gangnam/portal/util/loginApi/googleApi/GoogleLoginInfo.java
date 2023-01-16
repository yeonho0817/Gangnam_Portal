package com.gangnam.portal.util.loginApi.googleApi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class GoogleLoginInfo {
    private final GoogleConfigUtils googleConfigUtils;

    public ResponseEntity<Object> googleLoginUri() {
        String authUrl = googleConfigUtils.googleInitUrl();

        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);

            System.out.println(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }

    public UserInfoDto googleRedirectInfo(String authCode) {
        // HTTP 통신을 위해 RestTemplate 활용
        RestTemplate restTemplate = new RestTemplate();
        LoginRequest requestParams = LoginRequest.builder()
                .clientId(googleConfigUtils.getGoogleClientId())
                .clientSecret(googleConfigUtils.getGoogleSecret())
                .code(authCode)
                .redirectUri(googleConfigUtils.getGoogleRedirectUrl())
                .grantType("authorization_code")
                .build();

        try {
            // Http Header 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
            ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(googleConfigUtils.getGoogleTokenUrl(), httpRequestEntity, String.class);

            // ObjectMapper를 통해 String to Object로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // NULL이 아닌 값만 응답받기(NULL인 경우는 생략)
            LoginResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<LoginResponse>() {});

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(apiResponseJson.getBody());

            System.out.println(apiResponseJson.getBody());
            System.out.println(element.getAsJsonObject().get("access_token").getAsString());

            // 사용자의 정보는 JWT Token으로 저장되어 있고, Id_Token에 값을 저장한다.
            String jwtToken = googleLoginResponse.getIdToken();

            System.out.println(jwtToken);

            // JWT Token을 전달해 JWT 저장된 사용자 정보 확인
            String requestUrl = UriComponentsBuilder.fromHttpUrl(googleConfigUtils.getGoogleUserInfoUrl()).queryParam("id_token", jwtToken).toUriString();

            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            if(resultJson != null) {
                System.out.println(resultJson);
                UserInfoDto userInfoDto = objectMapper.readValue(resultJson, new TypeReference<>() {});

                System.out.println(userInfoDto.getEmail());

                return userInfoDto;
            }
            else {
                throw new Exception("Google OAuth failed!");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getGoogleAccessToken(String authCode) {
        String access_Token="";
        String reqURL = googleConfigUtils.getGoogleTokenUrl();

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+googleConfigUtils.getGoogleClientId()); // TODO REST_API_KEY 입력
            sb.append("&redirect_uri="+googleConfigUtils.getGoogleRedirectUrl()); // TODO 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + authCode);
            bw.write(sb.toString());
            bw.flush();

            System.out.println(reqURL);

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("result : " + responseCode);
            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }


}