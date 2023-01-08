package com.gangnam.portal.service;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import com.gangnam.portal.util.googleApi.GoogleLoginInfo;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final GoogleLoginInfo googleLoginInfo;
    private final JwtTokenProvider jwtTokenProvider;

    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;

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
            String refreshToken = jwtTokenProvider.generateRefreshToken(isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());

            return new ResponseData(Status.LOGIN_SUCCESS, new EmployeeDTO.LoginResponseDTO(accessToken, refreshToken));
        }

    }
}
