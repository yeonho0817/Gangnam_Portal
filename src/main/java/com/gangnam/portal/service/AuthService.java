package com.gangnam.portal.service;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.domain.RefreshToken;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.RefreshTokenRepository;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import com.gangnam.portal.repository.redis.RedisRepository;
import com.gangnam.portal.util.googleApi.GoogleLoginInfo;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import com.gangnam.portal.util.jwt.redis.AccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final GoogleLoginInfo googleLoginInfo;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;
    private final RedisRepository redisRepository;

    //구글 - 로그인 API 주소 넘기는 것
    public ResponseData login() {
        ResponseEntity<Object> responseEntity = googleLoginInfo.googleLoginUri();

        if (responseEntity == null) {
            return new ResponseData(Status.PROVIDER_REJECTED);
        } else {
            return new ResponseData(Status.PROVIDER_ACCEPTED, responseEntity);
        }
    }

    // 구글 로그인 -> 성공시 대시보드 (월별 출근 현황, 사진, 소속, 부서)
    public ResponseData googleRedirectInfo(String authCode) {
        // 구글 이메일 받아오기
        String employeeEmail = googleLoginInfo.googleRedirectInfo(authCode).getEmail();

        // Employee_Email 테이블에서 email이 존재하는지 검사
        Optional<EmployeeEmail> isExists = employeeEmailCustomRepository.isExists(employeeEmail);

        if (isExists.isEmpty()) {
            return new ResponseData(Status.LOGIN_FAILED);
        } else {
            // jwt 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());
            String refreshToken = jwtTokenProvider.generateRefreshToken();

            refreshTokenRepository.save(RefreshToken.builder()
                    .refreshToken(refreshToken)
                    .build()
            );

            return new ResponseData(Status.LOGIN_SUCCESS, new EmployeeDTO.LoginResponseDTO(accessToken, refreshToken));
        }

    }

    // 토큰 재발급
    @Transactional
    public ResponseData reissueToken(String accessToken, String refreshToken) {
        // filter에서 정상 처리 완료
        // refresh 기존 꺼 삭제
        // access, refresh 토큰 재발급

        String employeeEmail = jwtTokenProvider.getEmail(accessToken);

        Optional<EmployeeEmail> isExists = employeeEmailCustomRepository.isExists(employeeEmail);

        // 기존 Refresh 삭제
        Optional<RefreshToken> isTokenExists = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (isTokenExists.isPresent()) refreshTokenRepository.deleteByRefreshToken(refreshToken);

        // 새로운 jwt
        // 새로운 refresh token
        String issueAccessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());
        String issueRefreshToken = jwtTokenProvider.generateRefreshToken();

        refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken(issueRefreshToken)
                .expiration(jwtTokenProvider.getExpiration(refreshToken))
                .build()
        );

        return new ResponseData(Status.LOGIN_SUCCESS, new EmployeeDTO.LoginResponseDTO(issueAccessToken, issueRefreshToken));
    }

    // refresh token db에서 삭제 (스케줄러)
    @Scheduled(fixedRate = 1000)
    public void deleteRefreshToken() {
        refreshTokenRepository.deleteByExpirationLessThanEqual(LocalDateTime.now());
    }

    // 로그아웃
    public ResponseData logout(String accessToken, String refreshToken) {
        // 기존 Refresh 삭제
        Optional<RefreshToken> isTokenExists = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (isTokenExists.isPresent()) refreshTokenRepository.deleteByRefreshToken(refreshToken);


        accessToken = jwtTokenProvider.getResolveToken(accessToken);

        // redis에 access Token 등록
        Long remainExpiration = jwtTokenProvider.getRemainExpiration(accessToken);
        if (jwtTokenProvider.getRemainExpiration(accessToken) > 0) {
            redisRepository.save(new AccessToken(accessToken, remainExpiration));
        }

        return new ResponseData(Status.LOGOUT_SUCCESS);
    }
}
