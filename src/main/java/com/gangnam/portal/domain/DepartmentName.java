package com.gangnam.portal.domain;

import lombok.Getter;

@Getter
public enum DepartmentName {
    개발(1, "개발"),
    QA(2, "QA"),
    경영지원(3, "경영지원"),
    디자인(4, "디자인"),
    마케팅(5, "마케팅")
    ;

    private int code;
    private String name;

    DepartmentName(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
