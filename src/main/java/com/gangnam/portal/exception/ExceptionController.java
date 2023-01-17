package com.gangnam.portal.exception;

import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ErrorStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice(basePackages = "com.gangnam.portal.controller")
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e) {

        return makeErrorResponse(e.getBindingResult());
    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<ErrorResponse> bindException(BindException e) {
//        return makeErrorResponse(e.getBindingResult());
//    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.of(e.getErrorStatus());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponse(BindingResult bindingResult) {
        ErrorStatus errorStatus = null;
        String message = null;

        if (bindingResult.hasErrors()) {
            //DTO에 설정한 message값을 가져온다.
            message = bindingResult.getFieldError().getDefaultMessage();

            //DTO에 유효성체크를 걸어놓은 어노테이션명을 가져온다.
            String bindResultCode = bindingResult.getFieldError().getCode();

            switch (Objects.requireNonNull(bindResultCode)) {

                case "NotBlank": case "NotNull":
                    errorStatus = ErrorStatus.BLANK_ESSENTIAL_VALUE;
                    break;

                case "Pattern":
                    errorStatus = ErrorStatus.INVALID_PATTERN;
                    break;

            }
        }

        return ErrorResponse.of(errorStatus);
    }
}
