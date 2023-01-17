package com.gangnam.portal.util.jwt;

import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.util.jwt.customUserDetails.CustomUserDetailService;
import com.gangnam.portal.util.jwt.customUserDetails.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final RedisTemplate redisTemplate;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getResolveToken(request.getHeader(AUTHORIZATION_HEADER));
        String refreshToken = jwtTokenProvider.getResolveToken(request.getHeader(REFRESH_TOKEN_HEADER));

        System.out.println(refreshToken);
        try {

            if (accessToken == null) { // 아무 것도 없을 때
                request.setAttribute("exception", ErrorStatus.TOKEN_EMPTY);
                throw new NullPointerException();
            } else if (refreshToken == null) { // accessToken만 있을 때
                if (request.getRequestURI().equals("/auth/reissue")) {
                    request.setAttribute("exception", ErrorStatus.TOKEN_EMPTY);
                    throw new NullPointerException();
                }

                // 토큰 걸러냄
                getAuthentication(request, accessToken);

                // 로그아웃인지 확인
                String isLogout = (String)redisTemplate.opsForValue().get("Bearer " + accessToken);

                if (isLogout == null) {
                    saveUserInfo(request, accessToken);
                } else {
                    request.setAttribute("exception", ErrorStatus.LOGOUT_ALREADY);
                    throw new IllegalArgumentException();
                }

            } else if (request.getRequestURI().equals("/auth/reissue")){  // accessToken + refreshToken 일 때 (재발급)
                getAuthentication(request, refreshToken);

                saveUserInfo(request, refreshToken);
            }
        } catch (NoSuchElementException e) {
            request.setAttribute("exception", ErrorStatus.NOT_FOUND_EMAIL);
            throw new IllegalArgumentException();
        }

        filterChain.doFilter(request, response);
    }

    private void saveUserInfo(HttpServletRequest request, String token) {
        String email = jwtTokenProvider.getEmail(token);
        String provider = jwtTokenProvider.getProvider(token);

        CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(email, provider);

        processSecurity(request, userDetails);
    }

    private void getAuthentication(HttpServletRequest request, String token) {
        Claims claims = null;

        try {
            if (token == null) throw new NullPointerException();

            claims = jwtTokenProvider.extractAllClaims(token);

        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            request.setAttribute("exception", ErrorStatus.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", ErrorStatus.TOKEN_EXPIRED);
        } catch (JwtException e) {
            request.setAttribute("exception", ErrorStatus.TOKEN_SIGNATURE_ERROR);
        } catch (NullPointerException e) {
            request.setAttribute("exception", ErrorStatus.TOKEN_EMPTY);
        }
    }

    // 검증 과정에 예외가 발생하지 않았다면, 해당 유저의 정보를 SecurityContext에 넣음
    private void processSecurity(HttpServletRequest request, CustomUserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}