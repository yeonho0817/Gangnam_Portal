package com.gangnam.portal.exception;

import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

@RestControllerAdvice(basePackages = "com.gangnam.portal.controller")
@Slf4j
public class ExceptionController {

    @Value("${frontRedirect}")
    private String frontRedirect;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e) {

        return bindingError(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {
        return bindingError(e.getBindingResult());
    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletResponse response) throws UnsupportedEncodingException {
        printLog(e.getErrorStatus().getHttpStatus().value(), e.getErrorStatus().getHttpStatus().name(), e.getErrorStatus().getDescription());

        ResponseEntity<ErrorResponse> errorResponseEntity = ErrorResponse.of(e.getErrorStatus(), e.getErrorStatus().getDescription());

        if (e.getErrorStatus() == ErrorStatus.NOT_FOUND_LOGIN_EMAIL) {
            String encodeMessage = URLEncoder.encode(errorResponseEntity.getBody().getMessage(), "UTF-8");

            response.setHeader("Location", frontRedirect + "/beforeEnter?status=" + errorResponseEntity.getBody().getStatus() + "&code=" + errorResponseEntity.getBody().getError() + "&message=" + encodeMessage);
            response.setStatus(302);

            return null;
        }

        return ErrorResponse.of(e.getErrorStatus(), e.getErrorStatus().getDescription());
    }


    private ResponseEntity<ErrorResponse> bindingError(BindingResult bindingResult) {
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

        printLog(errorStatus.getHttpStatus().value(), errorStatus.getHttpStatus().name(), message);


        return ErrorResponse.of(errorStatus, message);
    }

    private void printLog(Integer errorStatus, String errorCode, String errorMessage) {
        log.error("Exception Handler\n\tError Status - {}\n\tError Code - {}\n\tError Message - {}", errorStatus, errorCode, errorMessage);
    }
}
