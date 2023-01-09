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
    public static final String REFRESH_TOKEN_HEADER = "RefreshToken";

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.getResolveToken(request.getHeader(AUTHORIZATION_HEADER));
        String refreshToken = jwtTokenProvider.getResolveToken(request.getHeader(REFRESH_TOKEN_HEADER));

        // if문으로 accessToken만 있을 때랑 accessToken + refreshToken 이 있을 때를 나눠서 처리해야 하네. + 로그아웃
        if (accessToken != null && refreshToken == null) { // accessToken만 있을 때
            // 빈 토큰 or 만료 or 유효하지 않는 토큰 걸러냄
            getAuthentication(request, accessToken);

            String email = jwtTokenProvider.getEmail(accessToken);
            String provider = jwtTokenProvider.getProvider(accessToken);

            if (email != null) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(email, provider);

                processSecurity(request, userDetails);
            }

        } else if (accessToken != null && refreshToken != null && request.getRequestURI().equals("/reissue")){  // accessToken + refreshToken 일 때
            // redis에서 찾아보고 없으면 만료된거임 -> 다시 로그인하라 해야 함
            // redis에 있으면 accessToken, refreshToken 재발급해서 처리
            getAuthentication(request, refreshToken);

        }

        filterChain.doFilter(request, response);
    }



    private void getAuthentication(HttpServletRequest request, String token) {
        Claims claims = null;

        try {
            if (token == null) throw new NullPointerException();

            claims = jwtTokenProvider.extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", Status.TOKEN_EXPIRED);
        } catch (JwtException e) {
            request.setAttribute("exception", Status.TOKEN_SIGNATURE_ERROR);
        } catch (NullPointerException e) {
            request.setAttribute("exception", Status.TOKEN_EMPTY);
        }
    }
    private boolean isLogout(String accessToken) {
//        if (logoutAccessTokenRedisRepository.existsById(accessToken)) {
//
//            throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
//        }

        return false;
    }

    // 검증 과정에 예외가 발생하지 않았다면, 해당 유저의 정보를 SecurityContext에 넣음
    private void processSecurity(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}