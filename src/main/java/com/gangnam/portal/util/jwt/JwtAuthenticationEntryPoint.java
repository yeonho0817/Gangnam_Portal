package com.gangnam.portal.util.jwt;

import com.gangnam.portal.dto.Response.Status;
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


        Status exception = (Status)request.getAttribute("exception");


//        catch (SignatureException e) {
//            request.setAttribute("exception", Status.TOKEN_SIGNATURE_ERROR);
//        } catch (MalformedJwtException ex) {
//            request.setAttribute("exception", Status.TOKEN_INVALID);
//        } catch (UnsupportedJwtException ex) {
//            request.setAttribute("exception", Status.TOKEN_INVALID);
//        } catch (IllegalArgumentException ex) {
//            request.setAttribute("exception", Status.TOKEN_INVALID);
//        } catch (ExpiredJwtException e) {
//            request.setAttribute("exception", Status.TOKEN_EXPIRED);
//        } catch (JwtException e) {
//            request.setAttribute("exception", Status.TOKEN_SIGNATURE_ERROR);
//        } catch (NullPointerException e) {
//            request.setAttribute("exception", Status.TOKEN_EMPTY);
//        }

        System.out.println("dtgddfsdfsdf");
        setResponse(response, exception);
//        if (exception == Status.TOKEN_INVALID) {
//            setResponse(response, Status.TOKEN_INVALID);
//        } else if (exception == Status.TOKEN_EMPTY) {
//            setResponse(response, Status.TOKEN_EMPTY);
//        } else if (exception == Status.TOKEN_EXPIRED) {
//            setResponse(response,Status.TOKEN_EXPIRED);
//        } else if (exception == Status.TOKEN_SIGNATURE_ERROR) {
//            setResponse(response, Status.TOKEN_SIGNATURE_ERROR);
//        }
    }

    private void setResponse(HttpServletResponse response, Status status) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.getWriter().println("{ \"code\" : \"" + status.getCode()
                + "\", \"status\" : \"" +  status.getHttpStatus().toString()
                + "\", \"message\" : \"" + status.getDescription()
                + "\"}");
    }
}