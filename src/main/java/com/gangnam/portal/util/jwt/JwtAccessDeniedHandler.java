package com.gangnam.portal.util.jwt;

import com.gangnam.portal.dto.Response.ErrorStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component

public class JwtAccessDeniedHandler implements AccessDeniedHandler{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.getWriter().println("{ \"code\" : \"" + ErrorStatus.TOKEN_DENIED.getCode()
                + "\", \"status\" : \"" +  ErrorStatus.TOKEN_DENIED.getHttpStatus().toString()
                + "\", \"message\" : \"" + ErrorStatus.TOKEN_DENIED.getDescription()
                + "\"}");
    }
}