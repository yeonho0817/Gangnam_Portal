package com.gangnam.portal.config;

import com.gangnam.portal.util.jwt.JwtAccessDeniedHandler;
import com.gangnam.portal.util.jwt.JwtAuthenticationEntryPoint;
import com.gangnam.portal.util.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return (web) -> web.ignoring()
//                .antMatchers(  "/css/**", "/images/**", "/js/**"
//                        // -- Swagger UI v2
//                        , "/v2/api-docs", "/swagger-resources/**"
//                        , "/swagger-ui.html", "/webjars/**", "/swagger/**"
//                        // -- Swagger UI v3 (Open API)
//                        , "/v3/api-docs/**", "/swagger-ui/**"
//                );
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()

                /**401, 403 Exception 핸들링 */
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                /**세션 사용하지 않음*/
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                /** HttpServletRequest를 사용하는 요청들에 대한 접근 제한 설정*/
                .and()
                .authorizeRequests()

                // cors 모두 허용 할려면 아래 입력해야 함.
                .mvcMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight Request 허용해주기

                // swagger
                .antMatchers(
                        "/css/**", "/images/**", "/js/**", "/favicon.ico"
                        // -- Swagger UI v2
                        , "/v2/api-docs", "/swagger-resources/**"
                        , "/swagger-ui.html", "/webjars/**", "/swagger/**"
                        // -- Swagger UI v3 (Open API)
                        , "/v3/api-docs/**", "/swagger-ui/**")
                .permitAll()


                // 로그인 API
                .antMatchers("/auth/google/login", "/auth/google/callback").permitAll()
                .antMatchers("/auth/kakao/login", "/auth/kakao/callback").permitAll()
                .antMatchers("/auth/logout").permitAll()
                
                //권한 부여
                .antMatchers("/commute/admin").hasRole("ADMIN")

                // 나머지는 다 인증 필요
                .anyRequest().authenticated()

                /**JwtSecurityConfig 적용 */
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }
}