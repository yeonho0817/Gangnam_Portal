package com.gangnam.portal.exception;

import com.gangnam.portal.dto.Response.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final ErrorStatus errorStatus;
}
