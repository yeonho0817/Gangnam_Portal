package com.gangnam.portal.dto.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ResponseData {
    private Status status;
    private Object data;

    public ResponseData(Status status, Object data) {
        this.status = status;
        this.data = data;
    }

    public ResponseData(Status status) {
        this.status = status;
        this.data = null;
    }
}
