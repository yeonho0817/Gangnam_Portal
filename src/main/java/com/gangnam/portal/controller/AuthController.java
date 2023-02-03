package com.gangnam.portal.controller;

import com.gangnam.portal.domain.Provider;
import com.gangnam.portal.dto.AuthDTO;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@Tag(name = "authorization")
public class AuthController {
    private final AuthService authService;

    //구글 - 로그인 API 주소 넘기는 것
    @GetMapping("/google/login")
    @Operation(operationId = "googleLoginApi", summary = "구글 로그인 API", description = "구글 로그인 URI를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구글 로그인 URI 반환",
                        content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "구글 로그인 URI 반환 실패",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<AuthDTO.LoginUriDTO> loginOfGoogle(HttpServletResponse response) {
        return authService.login(Provider.google);
    }

    // 구글 로그인 리다이렉트 -> 로그인 성공 시 서버 JWT 토큰 넘겨줌
    @GetMapping("/google/callback")
    @Operation(operationId = "googleRedirectApi", summary = "사원 인증 및 토근 반환", description = "구글에서 인증된 정보와 DB의 정보를 비교하여 토큰을 발급합니다.", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 정보 반환",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "사원 인증 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void redirectInfoOfGoogle(HttpServletResponse response, @RequestParam(value = "code") String authCode) {
        AuthDTO.TokenDTO tokenDto = authService.redirectLogin(authCode, Provider.google);

//        response.setHeader("Location", "http://tlc.gangnam-portal.kro.kr:3000/beforeEnter?status=" + 200 + "&accessToken=" + tokenDto.getAccessToken() + "&refreshToken=" + tokenDto.getRefreshToken() + "&role=" + tokenDto.getRole());
        response.setHeader("Location", "http://localhost:3000/beforeEnter?status=" + 200 + "&accessToken=" + tokenDto.getAccessToken() + "&refreshToken=" + tokenDto.getRefreshToken() + "&role=" + tokenDto.getRole());

        response.setStatus(302);
    }

    // 카카오
    @GetMapping("/kakao/login")
    @Operation(operationId = "kakaaoLoginApi", summary = "카카오 로그인 API", description = "카카오 로그인 URI를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 URI 반환",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "카카오 로그인 URI 반환",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<AuthDTO.LoginUriDTO> loginOfKakao() {
        return authService.login(Provider.kakao);
    }

    // 카카오 라다이렉트
    @GetMapping("/kakao/callback")
    @Operation(operationId = "kakaoRedirectApi", summary = "사원 인증 및 토큰 반환", description = "카카오에서 인증된 정보와 DB의 정보를 비교하여 토큰을 발급합니다.", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 정보 반환",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "사원 인증 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public void redirectInfoOfKakao(HttpServletResponse response, @RequestParam(value = "code") String authCode) {
        AuthDTO.TokenDTO tokenDto = authService.redirectLogin(authCode, Provider.kakao);

        response.setHeader("Location", "http://localhost:3000/beforeEnter?status=" + 200 + "&accessToken=" + tokenDto.getAccessToken() + "&refreshToken=" + tokenDto.getRefreshToken() + "&role=" + tokenDto.getRole());
//        response.setHeader("Location", "http://tlc.gangnam-portal.kro.kr:3000/beforeEnter?status=" + 200 + "&accessToken=" + tokenDto.getAccessToken() + "&refreshToken=" + tokenDto.getRefreshToken() + "&role=" + tokenDto.getRole());

        response.setStatus(302);
    }

    // 토큰 만료 시, refreshToken 받아서 갱신 or 권한 거부
    @GetMapping("/reissue")
    @Operation(operationId = "reissue", summary = "토큰 재발급", description = "토큰이 만료된 경우 인증 후 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "토큰 재발급 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<AuthDTO.TokenDTO> reissueToken(@ApiIgnore @Parameter(hidden = true) UsernamePasswordAuthenticationToken authenticationToken,
                                                       @ApiIgnore @Parameter(hidden = true) @RequestHeader("RefreshToken") String refreshToken) {
        return authService.reissueToken(authenticationToken, refreshToken);
    }

    // logout
    @GetMapping("/logout")
    @Operation(operationId = "logout", summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "로그아웃 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData logout(@ApiIgnore @RequestHeader("Authorization") String accessToken) {
       return authService.logout(accessToken);
    }

}
