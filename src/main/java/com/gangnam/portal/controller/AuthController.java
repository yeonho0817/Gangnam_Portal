package com.gangnam.portal.controller;

import com.gangnam.portal.domain.Provider;
import com.gangnam.portal.dto.AuthDTO;
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
//            @ApiResponse(responseCode = "4XX, 5XX", description = "구글 로그인 URI 반환 실패",
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<AuthDTO.LoginUriDTO> loginOfGoogle() {
        ResponseData responseData = authService.login(Provider.google);

        return responseData;
    }

    // 구글 로그인 리다이렉트 -> 로그인 성공 시 서버 JWT 토큰 넘겨줌
    @GetMapping("/google/callback")
    @Operation(operationId = "googleRedirectApi", summary = "사원 인증", description = "구글에서 인증된 정보와 DB의 정보를 비교합니다.", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "구글 로그인 리다이렉트",
                    content = {@Content(mediaType = "application/json")}),
    })
    public ResponseData<AuthDTO.TokenDTO> redirectInfoOfGoogle(@RequestParam(value = "code") String authCode) {
        ResponseData responseData = authService.redirectLogin(authCode, Provider.google);

        return responseData;
    }

    // 카카오
    @GetMapping("/kakao/login")
    @Operation(operationId = "kakaaoLoginApi", summary = "카카오 로그인 API", description = "카카오 로그인 URI를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 URI 반환",
                    content = {@Content(mediaType = "application/json")}),
    })
    public ResponseData<AuthDTO.LoginUriDTO> loginOfKakao() {
        return authService.login(Provider.kakao);
    }

    // 카카오 라다이렉트
    @GetMapping("/kakao/callback")
    @Operation(operationId = "kakaoRedirectApi", summary = "사원 인증", description = "카카오에서 인증된 정보와 DB의 정보를 비교합니다.", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카카오 로그인 리다이렉트",
                    content = {@Content(mediaType = "application/json")}),
    })
    public ResponseData<AuthDTO.TokenDTO> redirectInfoOfKakao(@RequestParam(value = "code") String authCode) {

        return authService.redirectLogin(authCode, Provider.kakao);
    }

    // 토큰 만료 시, refreshToken 받아서 갱신 or 권한 거부
    @GetMapping("/reissue")
    @Operation(operationId = "reissue", summary = "토큰 재발급", description = "토큰이 만료된 경우 인증된 후 재발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 재발급",
                    content = {@Content(mediaType = "application/json")}),
    })
    public ResponseData<AuthDTO.TokenDTO> reissueToken(@Parameter(hidden = true) UsernamePasswordAuthenticationToken authenticationToken,
                                                       @Parameter(hidden = true) @RequestHeader("RefreshToken") String refreshToken) {
        return authService.reissueToken(authenticationToken, refreshToken);
    }

    // logout
    @GetMapping("/logout")
    @Operation(operationId = "logout", summary = "로그아웃", description = "로그아웃합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
    })
    public ResponseData logout(@Parameter(hidden = true) @RequestHeader("Authorization") String accessToken) {
       return authService.logout(accessToken);
    }

}
