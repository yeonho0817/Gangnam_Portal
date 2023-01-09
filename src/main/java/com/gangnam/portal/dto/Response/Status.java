package com.gangnam.portal.dto.Response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Status {
    LOGIN_SUCCESS(200, HttpStatus.OK, "로그인을 성공했습니다."),
    LOGIN_FAILED(404, HttpStatus.NOT_FOUND, "로그인을 실패했습니다."),

    PROVIDER_ACCEPTED(406, HttpStatus.NOT_ACCEPTABLE, "해당 로그인은 정상적인 접속입니다."),
    PROVIDER_REJECTED(406, HttpStatus.NOT_ACCEPTABLE, "해당 로그인은 정상적인 접속이 아닙니다."),

    TOKEN_EMPTY(404, HttpStatus.NOT_FOUND, "Token이 없습니다. 다시 보내주세요!"),
    ACCESS_TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "AccessToken이 만료되었습니다. RefreshToken을 보내주세요!"),
    ACCESS_TOKEN_SIGNATURE_ERROR(406, HttpStatus.NOT_ACCEPTABLE, "Token의 Signature가 잘못됐습니다."),
    REFRESH_TOKEN_EXPIRED(401, HttpStatus.UNAUTHORIZED, "RefreshToken이 만료되었습니다. 다시 로그인해주세요!"),
    TOKEN_DENIED(401, HttpStatus.UNAUTHORIZED, "해당 사용자는 접속할 권한이 없습니다."),

    EMAIL_NOT_FOUND(404, HttpStatus.NOT_FOUND, "입력하신 이메일을 찾지 못했습니다."),
    PASSWORD_NOT_FOUND(404, HttpStatus.NOT_FOUND, "입력하신 비밀번호를 찾지 못했습니다."),
    FIND_PASSWORD_FAILED(404, HttpStatus.NOT_FOUND, "일치하는 이메일이 없습니다."),
    UPDATE_HUMAN_RESOURCE_SUCCESS(200, HttpStatus.OK, "회원 정보 수정을 완료했습니다."),
    UPDATE_HUMAN_RESOURCE_FAILED(409, HttpStatus.OK, "회원 정보 수정을 실패했습니다."),

    MONTHLY_COMMUTE_SUCCESS(200, HttpStatus.OK, "월별 출퇴근 조회를 완료했습니다."),
    COMMUTE_SUCCESS(200, HttpStatus.OK, "출퇴근 현황 조회를 완료했습니다."),
    COMMUTE_START_SUCCESS(200, HttpStatus.FORBIDDEN, "출근 등록을 완료했습니다."),
    COMMUTE_END_SUCCESS(200, HttpStatus.FORBIDDEN, "퇴근 등록을 완료했습니다."),
    COMMUTE_START_FAILED(409, HttpStatus.FORBIDDEN, "이미 출근한 상태입니다."),
    COMMUTE_END_FAILED(409, HttpStatus.FORBIDDEN, "이미 퇴근한 상태입니다."),
    COMMUTE_UPDATE_SUCCESS(200, HttpStatus.FORBIDDEN, "출퇴근 수정을 완료했습니다"),

    HUMAN_RESOURCE_SUCCESS(200, HttpStatus.OK, "인력 조회에 성공했습니다."),
    HUMAN_RESOURCE_DEPARTMENT_SUCCESS(200, HttpStatus.OK, "소속/부서 조회에 성공했습니다."),
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
