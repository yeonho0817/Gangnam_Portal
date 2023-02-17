package com.gangnam.portal.dto.Response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Status {

    // 로그인, 로그아웃
    LOGIN_SUCCESS(200, HttpStatus.OK, "로그인을 성공했습니다."),
    LOGOUT_SUCCESS(200, HttpStatus.OK, "로그아웃했습니다."),
    PROVIDER_ACCEPTED(200, HttpStatus.OK, "해당 로그인은 정상적인 접속입니다."),

    // 출퇴근 현황
    MONTHLY_COMMUTE_SUCCESS(200, HttpStatus.OK, "월별 출퇴근 조회를 완료했습니다."),
    COMMUTE_SUCCESS(200, HttpStatus.OK, "출퇴근 현황 조회를 완료했습니다."),
    COMMUTE_START_SUCCESS(200, HttpStatus.OK, "출근 등록을 완료했습니다."),
    COMMUTE_END_SUCCESS(200, HttpStatus.OK, "퇴근 등록을 완료했습니다."),
    COMMUTE_CREATE_SUCCESS(200, HttpStatus.OK, "출퇴근 등록을 완료했습니다"),
    COMMUTE_UPDATE_SUCCESS(200, HttpStatus.OK, "출퇴근 수정을 완료했습니다"),

    // 토큰 재발급
    TOKEN_REISSUE_SUCCESS(200, HttpStatus.OK, "토큰 재발급을 완료했습니다."),

    // 사원 관리
    SAVE_EMPLOYEE_SUCCESS(200, HttpStatus.OK, "사원 추가를 완료했습니다."),
    UPDATE_EMPLOYEE_SUCCESS(200, HttpStatus.OK, "사원 정보 수정을 완료했습니다."),

    // 인사 관리
    HUMAN_RESOURCE_SUCCESS(200, HttpStatus.OK, "인력 조회에 성공했습니다."),
    HUMAN_RESOURCE_DEPARTMENT_SUCCESS(200, HttpStatus.OK, "소속/부서 조회에 성공했습니다."),
    FIND_EMPLOYEE_SUCCESS(200, HttpStatus.OK, "해당 직원 정보를 성공적으로 조회했습니다."),
    FIND_MY_INFO_SUCCESS(200, HttpStatus.OK, "사원 정보를 성공적으로 조회했습니다."),
    UPDATE_MY_INFO_SUCCESS(200, HttpStatus.OK, "사원 정보 수정을 완료했습니다."),
    READ_SUCCESS(200, HttpStatus.OK, "정상적으로 조회했습니다."),

    ;

    Status(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }

    private int code;
    private HttpStatus httpStatus;
    private String description;
}
