package com.gangnam.portal.controller;

import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //구글 - 로그인 API 주소 넘기는 것
    @GetMapping("/google/login")
    public ResponseEntity loginOfGoogle() {
        ResponseData responseData = authService.loginOfGoogle();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 구글 로그인 리다이렉트 -> 로그인 성공 시 서버 JWT 토큰 넘겨줌
    @GetMapping("/auth/google/callback")
    public ResponseEntity redirectInfoOfGoogle(@RequestParam(value = "code") String authCode) {
        ResponseData responseData = authService.redirectInfoOfGoogle(authCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 네이버
    @GetMapping("/naver/login")
    public ResponseEntity loginOfNaver() {
        ResponseData responseData = authService.loginOfNaver();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 네이버 라다이렉트
    @GetMapping("/auth/naver/callback")
    public ResponseEntity redirectInfoOfNaver(@RequestParam(value = "code") String authCode) {
        ResponseData responseData = authService.redirectInfoOfNaver(authCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }



    // 카카오(?)


    // 토큰 만료 시, refreshToken 받아서 갱신 or 권한 거부
    @GetMapping("/reissue")
    public ResponseEntity reissueToken(@RequestHeader("Authorization") String accessToken,
                                       @RequestHeader("RefreshToken") String refreshToken) {
        ResponseData responseData = authService.reissueToken(accessToken, refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // logout
    @GetMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String accessToken,
                                 @RequestHeader("RefreshToken") String refreshToken) {
        ResponseData responseData = authService.logout(accessToken, refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

}
