package com.gangnam.portal.controller;

import com.gangnam.portal.domain.Provider;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    //구글 - 로그인 API 주소 넘기는 것
    @GetMapping("/google/login")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "구글 로그인 URI 반환",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseEntity loginOfGoogle() {
        ResponseData responseData = authService.login(Provider.google);

        return ResponseEntity
                .status(responseData.getStatus().getHttpStatus())
                .body(responseData.getData());
    }

    // 구글 로그인 리다이렉트 -> 로그인 성공 시 서버 JWT 토큰 넘겨줌
    @GetMapping("/auth/google/callback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구글 로그인 리다이렉트(서버)",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseEntity redirectInfoOfGoogle(@RequestParam(value = "code") String authCode) {
        ResponseData responseData = authService.redirectLogin(authCode, Provider.google);

        return ResponseEntity
                .status(responseData.getStatus().getHttpStatus())
                .body(responseData.getData());
    }

    // 카카오
    @GetMapping("/kakao/login")
    public ResponseEntity loginOfKakao() {
        ResponseData responseData = authService.login(Provider.kakao);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 카카오 라다이렉트
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity redirectInfoOfKakao(@RequestParam(value = "code") String authCode) {
        ResponseData responseData = authService.redirectLogin(authCode, Provider.kakao);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 토큰 만료 시, refreshToken 받아서 갱신 or 권한 거부
    @GetMapping("/reissue")
    public ResponseEntity reissueToken(UsernamePasswordAuthenticationToken authenticationToken,
//                                       @RequestHeader("Authorization") String accessToken,
                                       @RequestHeader("RefreshToken") String refreshToken) {
        ResponseData responseData = authService.reissueToken(authenticationToken, refreshToken);

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
