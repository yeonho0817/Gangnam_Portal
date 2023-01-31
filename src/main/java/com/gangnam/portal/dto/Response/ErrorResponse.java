package com.gangnam.portal.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Schema(description = "ErrorResponse")
@Getter
@Builder
public class ErrorResponse {
    @Schema(description = "Error Time")
    private final String localDateTime = LocalDateTime.now().toString();
    @Schema(description = "Error Status")
    private final Integer status;
    @Schema(description = "Error Name")
    private final String error;
    @Schema(description = "Error Message")
    private final String message;

    public static ResponseEntity<ErrorResponse> of(ErrorStatus errorStatus, String message) {
        return  ResponseEntity
                .status(errorStatus.getHttpStatus())
                .body(
                        ErrorResponse.builder()
                                .status(errorStatus.getHttpStatus().value())
                                .error(errorStatus.getHttpStatus().name())
                                .message(message)
                        .build()
                );
    }
}
