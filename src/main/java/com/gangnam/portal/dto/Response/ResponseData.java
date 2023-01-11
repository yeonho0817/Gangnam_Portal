package com.gangnam.portal.dto.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseData {
    private Status status;
    private String message;
    private Object data;

    public ResponseData(Status status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseData(Status status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
