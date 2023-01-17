package com.gangnam.portal.util.jwt;

import com.gangnam.portal.dto.Response.ErrorStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        ErrorStatus exception = (ErrorStatus)request.getAttribute("exception");

        if (exception == ErrorStatus.TOKEN_INVALID) {
            setResponse(response, ErrorStatus.TOKEN_INVALID);
        } else if (exception == ErrorStatus.TOKEN_EMPTY) {
            setResponse(response, ErrorStatus.TOKEN_EMPTY);
        } else if (exception == ErrorStatus.TOKEN_EXPIRED) {
            setResponse(response,ErrorStatus.TOKEN_EXPIRED);
        } else if (exception == ErrorStatus.TOKEN_SIGNATURE_ERROR) {
            setResponse(response, ErrorStatus.TOKEN_SIGNATURE_ERROR);
        } else if (exception == ErrorStatus.LOGOUT_ALREADY) {
            setResponse(response, ErrorStatus.LOGOUT_ALREADY);
        } else if (exception == ErrorStatus.NOT_FOUND_EMAIL) {
            setResponse(response, ErrorStatus.NOT_FOUND_EMAIL);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);


        response.getWriter().println("{ \"code\" : \"" + errorStatus.getCode()
                + "\", \"status\" : \"" +  errorStatus.getHttpStatus().toString()
                + "\", \"message\" : \"" + errorStatus.getDescription()
                + "\"}");
    }
}