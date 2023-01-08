package com.gangnam.portal.controller;

import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {
    private final EmployeeService employeeService;

    //구글 - 로그인 API 주소 넘기는 것
    @GetMapping("/google/login")
    public ResponseEntity login() {
        ResponseData responseData = employeeService.login();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 구글 로그인 리다이렉트 -> 로그인 성공 시 서버 JWT 토큰 넘겨줌
    @GetMapping("/auth/google/callback")
    public ResponseEntity googleRedirectInfo(@RequestParam(value = "code") String authCode) {
        ResponseData responseData = employeeService.googleRedirectInfo(authCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 네이버


    // 카카오(?)


    // 토큰 만료 시, refreshToken 받아서 갱신 or 권한 거부
    @GetMapping("/reissue")
    public ResponseEntity reissueRefreshToken(@RequestHeader("Authorization") String refreshToken) {
        return null;
    }
}
