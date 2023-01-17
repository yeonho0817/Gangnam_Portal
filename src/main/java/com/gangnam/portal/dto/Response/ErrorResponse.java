package com.gangnam.portal.dto.Response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private final String localDateTime;
    private final Integer status;
    private final String error;
    private final String message;

    public static ResponseEntity<ErrorResponse> of(ErrorStatus errorStatus) {
        return  ResponseEntity
                .status(errorStatus.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .localDateTime(LocalDateTime.now().toString())
                                .status(errorStatus.getHttpStatus().value())
                                .error(errorStatus.getHttpStatus().name())
                                .message(errorStatus.getDescription())
                        .build()
                );
    }
}
