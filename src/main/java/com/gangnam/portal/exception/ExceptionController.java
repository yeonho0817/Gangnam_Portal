package com.gangnam.portal.exception;

import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestControllerAdvice(basePackages = "com.gangnam.portal.controller")
@Slf4j
public class ExceptionController {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e) {

        return bindingError(e.getBindingResult());
    }

//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<ErrorResponse> bindException(BindException e) {
//        return makeErrorResponse(e.getBindingResult());
//    }

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletResponse response) {
        printLog(e.getErrorStatus().getHttpStatus().value(), e.getErrorStatus().getHttpStatus().name(), e.getErrorStatus().getDescription());

        ResponseEntity<ErrorResponse> errorResponseEntity = ErrorResponse.of(e.getErrorStatus());

        if (e.getErrorStatus() == ErrorStatus.NOT_FOUND_EMAIL) {
            response.setHeader("Location", "http://localhost:3000/beforeEnter?status=" + errorResponseEntity.getBody().getStatus());
            response.setHeader("ErrorStatus", errorResponseEntity.getBody().getStatus().toString());
            response.setHeader("ErrorCode", errorResponseEntity.getBody().getError());
            response.setHeader("ErrorMessage", errorResponseEntity.getBody().getMessage());
            response.setStatus(302);

            return null;
        }


        return ErrorResponse.of(e.getErrorStatus());
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


        return ErrorResponse.of(errorStatus);
    }

    private void printLog(Integer errorStatus, String errorCode, String errorMessage) {
        log.error("Exception Handler\n\tError Status - {}\n\tError Code - {}\n\tError Message - {}", errorStatus, errorCode, errorMessage);
    }
}
