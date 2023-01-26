package com.gangnam.portal.service;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.domain.Provider;
import com.gangnam.portal.dto.AuthDTO;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import com.gangnam.portal.util.loginApi.googleApi.GoogleLoginInfo;
import com.gangnam.portal.util.loginApi.kakaoApi.KaKaoLoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final GoogleLoginInfo googleLoginInfo;
    private final KaKaoLoginInfo kaKaoLoginInfo;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;

    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;

    // 로그인 API
    public ResponseData<AuthDTO.LoginUriDTO> login(Provider provider) {
        ResponseEntity<Object> responseEntity = null;

        if (provider == Provider.google) {
            responseEntity = googleLoginInfo.googleLoginUri();
        } else if (provider == Provider.kakao) {
            responseEntity = kaKaoLoginInfo.kakaoLoginUri();
        }

        Optional.ofNullable(responseEntity)
                .orElseThrow(() -> new CustomException(ErrorStatus.PROVIDER_REJECTED));

        return new ResponseData<>(Status.PROVIDER_ACCEPTED, Status.PROVIDER_ACCEPTED.getDescription(), new AuthDTO.LoginUriDTO(responseEntity.getHeaders().get("Location").get(0)));
    }

    @Transactional(rollbackFor = {Exception.class})
    public AuthDTO.TokenDTO redirectLogin(String authCode, Provider provider) {
        String email = null;
        EmployeeEmail isExists = null;

        if (provider == Provider.google) {
            String googleAccessToken = googleLoginInfo.getGoogleAccessToken(authCode);

            email = googleLoginInfo.getGoogleUserInfo(googleAccessToken);

            isExists = employeeEmailCustomRepository.isExists(email, Provider.google.name())
                    .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_LOGIN_EMAIL));

        } else if (provider == Provider.kakao) {
            String kakaoAccessToken = kaKaoLoginInfo.getKaKaoAccessToken(authCode);

            email = kaKaoLoginInfo.getKakaoUserInfo(kakaoAccessToken);

            isExists = employeeEmailCustomRepository.isExists(email, Provider.kakao.name())
                    .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_LOGIN_EMAIL));
        }

        // jwt 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(isExists.getEmployee().getId(), isExists.getEmail(), isExists.getProvider().toString(), isExists.getEmployee().getAuthority().getName().name());
        String refreshToken = jwtTokenProvider.generateRefreshToken(isExists.getEmployee().getId(), isExists.getEmail(), isExists.getProvider().toString(), isExists.getEmployee().getAuthority().getName().name());

        // refresh -> redis에 저장
        saveRedis("RT:" + isExists.getEmail() + "-" + isExists.getProvider().name(), refreshToken,jwtTokenProvider.getExpiration(jwtTokenProvider.getResolveToken(refreshToken)) - new Date().getTime());

        return new AuthDTO.TokenDTO(accessToken, refreshToken, isExists.getEmployee().getAuthority().getName().name());
    }

    // 토큰 재발급
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData<AuthDTO.TokenDTO> reissueToken(UsernamePasswordAuthenticationToken authenticationToken, String refreshToken) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        // refresh token 비교 -> access token 재생성, refresh token 업데이트
        String email = authenticationDTO.getEmail();
        String provider = authenticationDTO.getProvider();

        String findRefreshToken = (String)redisTemplate.opsForValue().get("RT:" + email + "-" + provider);
        if(!refreshToken.equals(findRefreshToken)) throw new CustomException(ErrorStatus.TOKEN_NOT_COINCIDE);

        // 새로운 jwt
        String issueAccessToken = jwtTokenProvider.generateAccessToken(authenticationDTO.getId(), authenticationDTO.getEmail(), authenticationDTO.getProvider(), authenticationDTO.getRole());

        // refresh token 지우고 재생성
        String issueRefreshToken = jwtTokenProvider.generateRefreshToken(authenticationDTO.getId(), authenticationDTO.getEmail(), authenticationDTO.getProvider(), authenticationDTO.getRole());

        deleteRedis("RT:" + email + "-" + provider);
        saveRedis("RT:" + authenticationDTO.getEmail() + "-" + authenticationDTO.getProvider(), issueRefreshToken,jwtTokenProvider.getExpiration(jwtTokenProvider.getResolveToken(refreshToken)) - new Date().getTime());

        return new ResponseData<>(Status.LOGIN_SUCCESS, Status.LOGIN_SUCCESS.getDescription(), new AuthDTO.TokenDTO(issueAccessToken, issueRefreshToken, authenticationDTO.getRole()));
    }


    // 로그아웃
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData logout(String accessToken) {
        // refresh 삭제 -> access Redis에 등록
        String resolveAccessToken = jwtTokenProvider.getResolveToken(accessToken);

        String email = jwtTokenProvider.getEmail(resolveAccessToken);
        String provider = jwtTokenProvider.getProvider(resolveAccessToken);

        // Refresh Token 삭제
        if (redisTemplate.opsForValue().get("RT:" + email + "-" + provider) != null) {
            deleteRedis("RT:" + email + "-" + provider);
        }

        // redis에 access Token 등록
        Long remainExpiration = jwtTokenProvider.getRemainExpiration(resolveAccessToken);
        if (jwtTokenProvider.getRemainExpiration(resolveAccessToken) > 0) {
            saveRedis(accessToken, "logout", remainExpiration);
        }

        return new ResponseData<>(Status.LOGOUT_SUCCESS, Status.LOGOUT_SUCCESS.getDescription());
    }

    private void saveRedis(Object key, Object value, Long timeout) {
        redisTemplate.opsForValue()
                .set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    private void deleteRedis(Object key) {
        redisTemplate.delete(key);
    }

}
