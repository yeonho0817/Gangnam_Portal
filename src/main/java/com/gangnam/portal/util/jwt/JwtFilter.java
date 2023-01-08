package com.gangnam.portal.util.jwt;

import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.util.jwt.customUserDetails.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getToken(request);

        // 빈 토큰 or 만료 or 유효하지 않는 토큰 걸러냄
        getAuthentication(request, accessToken);

        // if문으로 accessToken만 있을 때랑 accessToken + refreshToken 이 있을 때를 나눠서 처리해야 하네.
            
//                checkLogout(accessToken);

        String email = jwtTokenProvider.getEmail(accessToken);

        if (email != null) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(email);

            processSecurity(request, userDetails);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void getAuthentication(HttpServletRequest request, String accessToken) {
        Claims claims = null;

        try {
            if (accessToken == null) throw new NullPointerException();

            claims = jwtTokenProvider.extractAllClaims(accessToken);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", Status.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException e) {
            request.setAttribute("exception", Status.ACCESS_TOKEN_SIGNATURE_ERROR);
        } catch (NullPointerException e) {
            request.setAttribute("exception", Status.TOKEN_EMPTY);
        }

    }

//        private void checkLogout(String accessToken) {
//            if (logoutAccessTokenRedisRepository.existsById(accessToken)) {
//                throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
//            }
//        }


    // 검증 과정에 예외가 발생하지 않았다면, 해당 유저의 정보를 SecurityContext에 넣음
    private void processSecurity(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}