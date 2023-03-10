package com.gangnam.portal.util.jwt;

import com.gangnam.portal.util.jwt.userDetails.CustomUserDetailService;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenUtil jwtTokenUtil;
//    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getToken(request);

        if (accessToken != null) {
//            checkLogout(accessToken);

            String userId = jwtTokenUtil.getUserId(accessToken);
            if (userId != null) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);
                userDetails = customUserDetailService.loadUserByUsername(userId);

                validateAccessToken(accessToken, userDetails);
                processSecurity(request, userDetails);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

//    private void checkLogout(String accessToken) {
//        if (logoutAccessTokenRedisRepository.existsById(accessToken)) {
//            throw new IllegalArgumentException("?????? ??????????????? ???????????????.");
//        }
//    }

    private void validateAccessToken(String accessToken, UserDetails userDetails) {
        if (!jwtTokenUtil.validateToken(accessToken, userDetails)) {
            throw new IllegalArgumentException("?????? ?????? ??????");
        }
    }

    private void processSecurity(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}