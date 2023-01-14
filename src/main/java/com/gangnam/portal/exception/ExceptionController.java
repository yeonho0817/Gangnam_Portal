package com.gangnam.portal.exception;

import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice(basePackages = "com.gangnam.portal.controller")
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity methodValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        ResponseData responseData = makeErrorResponse(e.getBindingResult());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity bindException(BindException e, HttpServletRequest request) {

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

                case "NotBlank": case "NotNull":
                    status = Status.BLANK_ESSENTIAL_VALUE;
                    break;

                case "Pattern":
                    status = Status.INVALID_PATTERN;
                    break;

            }
        }

        return new ResponseData(status, status.getDescription(), message);
    }
}
