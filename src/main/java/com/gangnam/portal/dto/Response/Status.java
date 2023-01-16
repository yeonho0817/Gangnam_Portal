package com.gangnam.portal.dto.Response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Status {
    
    // 로그인, 로그아웃
    LOGIN_SUCCESS(200, HttpStatus.OK, "로그인을 성공했습니다."),
    LOGIN_FAILED(404, HttpStatus.NOT_FOUND, "로그인을 실패했습니다."),

    LOGOUT_SUCCESS(200, HttpStatus.OK, "로그아웃했습니다."),
    LOGOUT_ALREADY(401, HttpStatus.UNAUTHORIZED, "이미 로그아웃한 정보입니다."),

    // 이메일 검색
    NOT_FOUND_EMAIL(404, HttpStatus.NOT_FOUND, "해당 이메일을 찾지 못했습니다."),
    NOT_FOUND_EMPLOYEE(404, HttpStatus.NOT_FOUND, "해당 직원을 찾지 못했습니다."),

    PROVIDER_ACCEPTED(406, HttpStatus.NOT_ACCEPTABLE, "해당 로그인은 정상적인 접속입니다."),
    PROVIDER_REJECTED(406, HttpStatus.NOT_ACCEPTABLE, "해당 로그인은 정상적인 접속이 아닙니다."),

    // 토큰 정보
    TOKEN_EMPTY(404, HttpStatus.NOT_FOUND, "Token이 없습니다. 다시 보내주세요!"),
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "해당 Token이 만료되었습니다."),
    TOKEN_SIGNATURE_ERROR(406, HttpStatus.NOT_ACCEPTABLE, "Token의 Signature가 잘못됐습니다."),
    TOKEN_DENIED(401, HttpStatus.UNAUTHORIZED, "해당 사용자는 접속할 권한이 없습니다."),
    TOKEN_NOT_COINCIDE(400, HttpStatus.BAD_REQUEST, "Token 정보가 일치하지 않습니다."),
    TOKEN_INVALID(400, HttpStatus.BAD_REQUEST, "Token 정보가 유효하지 않습니다."),

    //
    EMPLOYEE_NOT_FOUND(404, HttpStatus.NOT_FOUND, "입력하신 이메일을 찾지 못했습니다."),
    FIND_EMPLOYEE_SUCCESS(200, HttpStatus.OK, "해당 직원 정보를 성공적으로 조회했습니다."),
    
    // 사원 정보
    UPDATE_EMPLOYEE_INFO_SUCCESS(200, HttpStatus.OK, "회원 정보 수정을 완료했습니다."),
    UPDATE_EMPLOYEE_INFO_FAILED(409, HttpStatus.OK, "회원 정보 수정을 실패했습니다."),

    // 출퇴근 현황
    MONTHLY_COMMUTE_SUCCESS(200, HttpStatus.OK, "월별 출퇴근 조회를 완료했습니다."),
    COMMUTE_SUCCESS(200, HttpStatus.OK, "출퇴근 현황 조회를 완료했습니다."),
    COMMUTE_READ_FAILED(404, HttpStatus.NOT_FOUND, "해당 출퇴근 정보가 없습니다."),
    COMMUTE_START_SUCCESS(200, HttpStatus.FORBIDDEN, "출근 등록을 완료했습니다."),
    COMMUTE_END_SUCCESS(200, HttpStatus.FORBIDDEN, "퇴근 등록을 완료했습니다."),
    COMMUTE_ALREADY_START(409, HttpStatus.FORBIDDEN, "이미 출근한 상태입니다."),
    COMMUTE_ALREADY_END(409, HttpStatus.FORBIDDEN, "이미 퇴근한 상태입니다."),
    COMMUTE_START_FORBIDDEN(409, HttpStatus.FORBIDDEN, "아직 퇴근을 하지 않았습니다."),
    COMMUTE_END_FORBIDDEN(409, HttpStatus.FORBIDDEN, "아직 출근을 하지 않았습니다."),
    COMMUTE_UPDATE_SUCCESS(200, HttpStatus.FORBIDDEN, "출퇴근 수정을 완료했습니다"),

    // 인사조회
    HUMAN_RESOURCE_SUCCESS(200, HttpStatus.OK, "인력 조회에 성공했습니다."),
    HUMAN_RESOURCE_DEPARTMENT_SUCCESS(200, HttpStatus.OK, "소속/부서 조회에 성공했습니다."),

    READ_SUCCESS(200, HttpStatus.OK, "정상적으로 조회했습니다."),

    // Valid Error Message
    BLANK_ESSENTIAL_VALUE(400, HttpStatus.BAD_REQUEST, "필수 값이 없습니다."),
    INVALID_PATTERN(400, HttpStatus.BAD_REQUEST, "형식이 맞지 않습니다."),

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
