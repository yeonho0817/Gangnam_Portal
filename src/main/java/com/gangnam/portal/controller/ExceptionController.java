package com.gangnam.portal.controller;

import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        ResponseData responseData = makeErrorResponse(e.getBindingResult());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    private ResponseData makeErrorResponse(BindingResult bindingResult) {
        Status status = null;
        String message = null;

        if (bindingResult.hasErrors()) {
            //DTO에 설정한 message값을 가져온다.
            message = bindingResult.getFieldError().getDefaultMessage();

            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();

            switch (Objects.requireNonNull(bindResultCode)) {

                case "NotBlank":
                    status = Status.BLANK_ESSENTIAL_VALUE;
                    break;

            }
        }

        return new ResponseData(status, message);
    }
}
