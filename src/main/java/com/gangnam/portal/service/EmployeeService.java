package com.gangnam.portal.service;

import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;

//    private final GoogleLoginInfo googleLoginInfo;
//    private final JwtTokenProvider jwtTokenProvider;
//
//    //구글 - 로그인 API 주소 넘기는 것
//    public ResponseData login() {
//        ResponseEntity<Object> responseEntity = googleLoginInfo.googleLoginUri();
//
//        if (responseEntity == null) {
//            return new ResponseData(Status.PROVIDER_REJECTED);
//        } else {
//            return new ResponseData(Status.PROVIDER_ACCEPTED, responseEntity);
//        }
//    }
//
//    // 구글 로그인 -> 성공시 대시보드 (월별 출근 현황, 사진, 소속, 부서)
//    public ResponseData googleRedirectInfo(String authCode) {
//        // 구글 이메일 받아오기
//        String employeeEmail = googleLoginInfo.googleRedirectInfo(authCode).getEmail();
//
//        // Employee_Email 테이블에서 email이 존재하는지 검사
//        Optional<EmployeeEmail> isExists = employeeEmailCustomRepository.isExists(employeeEmail);
//
//        if (isExists.isEmpty()) {
//            return new ResponseData(Status.LOGIN_FAILED);
//        } else {
//            // jwt 토큰 생성
//            String accessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());
//            String refreshToken = jwtTokenProvider.generateRefreshToken(isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());
//
//            return new ResponseData(Status.LOGIN_SUCCESS, new EmployeeDTO.LoginResponseDTO(accessToken, refreshToken));
//        }
//
//    }

    // 로그아웃
    public void logout() {

    }

    // PW 찾기
    public void findPassword() {

    }

    // 회원 조회
    public void findEmployeeInfo() {

    }

    // 회원 수정
    public void findEmployeeInfoUpdate() {

    }
}
