package com.gangnam.portal.domain;

import lombok.Getter;

@Getter
public enum DepartmentName {
    개발(1, "개발"),
    퍼블리셔(1, "퍼블리셔"),
    QA(2, "QA"),
    재무(3, "재무"),
    인사총무(3, "인사총무"),
    UIUX(4, "UI/UX"),
    BX(5, "BX")
    ;

    private int code;
    private String name;

    DepartmentName(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
