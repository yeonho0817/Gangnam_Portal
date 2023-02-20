package com.gangnam.portal.dto.Response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus {
    LOGIN_FAILED(404, HttpStatus.NOT_FOUND, "로그인을 실패했습니다."),
    LOGOUT_ALREADY(401, HttpStatus.UNAUTHORIZED, "이미 로그아웃한 정보입니다."),
    PROVIDER_REJECTED(406, HttpStatus.NOT_ACCEPTABLE, "해당 로그인은 정상적인 접속이 아닙니다."),

    DUPLICATED_GOOGLE_EMAIL(409, HttpStatus.CONFLICT, "이미 가입된 회사 이메일입니다."),
    DUPLICATED_KAKAO_EMAIL(409, HttpStatus.CONFLICT, "이미 가입된 카카오 이메일입니다."),
    DUPLICATED_EMPLOYEE_NO(409, HttpStatus.CONFLICT, "이미 가입된 사번입니다."),
    DUPLICATED_PHONE(409, HttpStatus.CONFLICT, "이미 가입된 전화번호입니다."),

    FIND_AUTHORITY_FAILED(404, HttpStatus.NOT_FOUND, "해당 권한 정보가 없습니다."),
    FIND_RANK_FAILED(404, HttpStatus.NOT_FOUND, "해당 직급 정보가 없습니다."),
    FIND_DEPARTMENT_FAILED(404, HttpStatus.NOT_FOUND, "해당 소속/부서 정보가 없습니다."),

    SAVE_PROFILE_IMAGE_FAILED(409, HttpStatus.CONFLICT, "프로필 저장에 실패했습니다."),
    NOT_FOUND_PROFILE_IMAGE(404, HttpStatus.NOT_FOUND, "프로필 이미지를 가져올 수 없습니다."),

    NOT_FOUND_LOGIN_EMAIL(401, HttpStatus.UNAUTHORIZED, "로그인한 이메일을 찾지 못했습니다."),
    NOT_FOUND_EMAIL(404, HttpStatus.NOT_FOUND, "해당 이메일을 찾지 못했습니다."),
    NOT_FOUND_EMPLOYEE(404, HttpStatus.NOT_FOUND, "해당 직원을 찾지 못했습니다."),

    TOKEN_NOT_COINCIDE(401, HttpStatus.UNAUTHORIZED, "Token 정보가 일치하지 않습니다."),
    TOKEN_INVALID(401, HttpStatus.UNAUTHORIZED, "Token 정보가 유효하지 않습니다."),
    TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "해당 Token이 만료되었습니다."),
    TOKEN_SIGNATURE_ERROR(401, HttpStatus.UNAUTHORIZED, "Token의 Signature가 잘못됐습니다."),
    TOKEN_EMPTY(401, HttpStatus.UNAUTHORIZED, "Token이 없습니다. 다시 보내주세요!"),
    TOKEN_DENIED(403, HttpStatus.FORBIDDEN, "해당 사용자는 접속할 권한이 없습니다."),

    COMMUTE_READ_FAILED(404, HttpStatus.NOT_FOUND, "해당 출퇴근 정보가 없습니다."),
    COMMUTE_START_FORBIDDEN(409, HttpStatus.CONFLICT, "아직 퇴근을 하지 않았습니다."),
    COMMUTE_ALREADY_START(409, HttpStatus.CONFLICT, "이미 출근한 상태입니다."),
    COMMUTE_END_FORBIDDEN(409, HttpStatus.CONFLICT, "아직 출근을 하지 않았습니다."),
    COMMUTE_ALREADY_END(409, HttpStatus.CONFLICT, "이미 퇴근한 상태입니다."),
    COMMUTE_ALREADY_EXISTS(409, HttpStatus.CONFLICT, "이미 해당 날짜의 출퇴근 정보가 존재합니다."),

    COMMUTE_REGISTER_DATE_ERROR(409, HttpStatus.CONFLICT, "등록일이 오늘을 넘을 수 없습니다."),
    COMMUTE_END_DATE_ERROR(409, HttpStatus.CONFLICT, "퇴근 시간이 출근 시간보다 빠를 수 없습니다."),
    COMMUTE_DATE_ERROR(409, HttpStatus.CONFLICT, "출근이나 퇴근 시간이 현재 시간을 넘을 수 없습니다."),

    WEATHER_INFO_FAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "날씨 정보를 가져올 수 없습니다."),
    FORTUNE_INFO_FAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "운세 정보를 가져올 수 없습니다."),
    SUBWAY_INFO_FAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "지하철 정보를 가져올 수 없습니다."),
    HOLIDAY_INFO_FAIL(500, HttpStatus.INTERNAL_SERVER_ERROR, "휴일 정보를 가져올 수 없습니다."),

    // Valid Error Message
    BLANK_ESSENTIAL_VALUE(400, HttpStatus.BAD_REQUEST, "필수 값이 없습니다."),
    INVALID_PATTERN(400, HttpStatus.BAD_REQUEST, "형식이 맞지 않습니다."),
    INVALID_MIN(400, HttpStatus.BAD_REQUEST, "최소값 보다 커야합니다."),
    INVALID_SIZE(400, HttpStatus.BAD_REQUEST, "길이가 맞지 않습니다.");

    ;

    ErrorStatus(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }

    private int code;
    private HttpStatus httpStatus;
    private String description;
}
