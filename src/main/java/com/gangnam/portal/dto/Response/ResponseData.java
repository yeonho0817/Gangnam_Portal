package com.gangnam.portal.dto.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseData<T> {
    private Status status;
    private String message;
    private T data;

    public ResponseData(Status status, String message, T data) {
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
