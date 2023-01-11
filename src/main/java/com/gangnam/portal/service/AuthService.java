package com.gangnam.portal.service;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.domain.Provider;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import com.gangnam.portal.repository.redis.RedisRepository;
import com.gangnam.portal.util.jwt.JwtExpirationEnums;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import com.gangnam.portal.util.jwt.redis.RefreshToken;
import com.gangnam.portal.util.loginApi.googleApi.GoogleLoginInfo;
import com.gangnam.portal.util.loginApi.kakaoApi.KaKaoLoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final RedisRepository redisRepository;

    // 로그인 API
    public ResponseData login(Provider provider) {
        ResponseEntity<Object> responseEntity = null;

        if (provider == Provider.google) {
            responseEntity = googleLoginInfo.googleLoginUri();
        } else if (provider == Provider.kakao) {
            responseEntity = kaKaoLoginInfo.kakaoLoginUri();
        }

        if (responseEntity == null) {
            return new ResponseData(Status.PROVIDER_REJECTED, Status.PROVIDER_REJECTED.getDescription());
        } else {
            return new ResponseData(Status.PROVIDER_ACCEPTED, Status.PROVIDER_ACCEPTED.getDescription(), responseEntity);
        }
    }

    public ResponseData redirectLogin(String authCode, Provider provider) {
        String email = null;
        Optional<EmployeeEmail> isExists = null;

        if (provider == Provider.google) {
            email = googleLoginInfo.googleRedirectInfo(authCode).getEmail();

            isExists = employeeEmailCustomRepository.isExists(email, Provider.google.name());
        } else if (provider == Provider.kakao) {
            String kakaoAccessToken = kaKaoLoginInfo.getKaKaoAccessToken(authCode);

            email = kaKaoLoginInfo.getKakaoUserInfo(kakaoAccessToken);

            isExists = employeeEmailCustomRepository.isExists(email, Provider.kakao.name());
        }

        if (isExists.isEmpty()) {
            return new ResponseData(Status.LOGIN_FAILED, Status.LOGIN_FAILED.getDescription());
        } else {
            // jwt 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmployee().getId(), isExists.get().getEmail(), isExists.get().getProvider().toString());
            String refreshToken = jwtTokenProvider.generateRefreshToken();

            // refresh -> redis에 저장
            saveRedis("RT:" + isExists.get().getEmail() + "-" + isExists.get().getProvider().name(),
                    refreshToken,
                    JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue(),
                    TimeUnit.MILLISECONDS
            );

//            redisTemplate.opsForValue()
//                    //key  value  timeout  unit
//                    .set("RT:" + isExists.get().getEmail() + "-" + isExists.get().getProvider().name(), refreshToken,
//                    JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);

            redisRepository.save(new RefreshToken(refreshToken, isExists.get().getEmail(), jwtTokenProvider.getExpiration(jwtTokenProvider.getResolveToken(refreshToken)).getTime()));

            return new ResponseData(Status.LOGIN_SUCCESS, Status.LOGIN_SUCCESS.getDescription(), new EmployeeDTO.LoginResponseDTO(accessToken, refreshToken, isExists.get().getEmployee().getAuthority().getName().name()));
        }
    }

    // 토큰 재발급
    @Transactional
    public ResponseData reissueToken(String accessToken, String refreshToken) {
        // filter에서 정상 처리 완료
        // refresh token 비교 -> access token 재생성, refresh token 업데이트

        String email = jwtTokenProvider.getEmail(accessToken);
        String provider = jwtTokenProvider.getProvider(accessToken);

        Optional<EmployeeEmail> isExists = employeeEmailCustomRepository.isExists(email, provider);
        if (isExists.isEmpty()) return new ResponseData(Status.NOT_FOUND_EMAIL, Status.NOT_FOUND_EMAIL.getDescription());

        String findRefreshToken = (String)redisTemplate.opsForValue().get("RT:" + email + "-" + provider);
        if(!refreshToken.equals(refreshToken)) return new ResponseData(Status.TOKEN_NOT_COINCIDE, Status.TOKEN_NOT_COINCIDE.getDescription());

        // 새로운 jwt
        String issueAccessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmployee().getId(), isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());

        // refresh token 지우고 재생성
        String issueRefreshToken = jwtTokenProvider.generateRefreshToken();

        deleteRedis("RT:" + email + "-" + provider);
        saveRedis("RT:" + isExists.get().getEmail() + "-" + isExists.get().getProvider().name(),
                issueRefreshToken,
                JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue(),
                TimeUnit.MILLISECONDS
        );

//        redisTemplate.delete("RT:" + email + "-" + provider);
//        redisTemplate.opsForValue()
//                .set("RT:" + isExists.get().getEmail() + "-" + isExists.get().getProvider().name()
//                        , issueRefreshToken,
//                        JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);

        return new ResponseData(Status.LOGIN_SUCCESS, Status.LOGIN_SUCCESS.getDescription(), new EmployeeDTO.LoginResponseDTO(issueAccessToken, issueRefreshToken, isExists.get().getEmployee().getAuthority().getName().name()));
    }


    // 로그아웃
    public ResponseData logout(String accessToken, String refreshToken) {
        // refresh 삭제 -> access Redis에 등록
        String resolveAccessToken = jwtTokenProvider.getResolveToken(accessToken);

        String email = jwtTokenProvider.getEmail(resolveAccessToken);
        String provider = jwtTokenProvider.getProvider(resolveAccessToken);

        if (redisTemplate.opsForValue().get("RT:" + email + "-" + provider) != null) {
            // Refresh Token 삭제
            deleteRedis("RT:" + email + "-" + provider);
//            redisTemplate.delete("RT:" + email + "-" + provider);
        }

        // redis에 access Token 등록
        Long remainExpiration = jwtTokenProvider.getRemainExpiration(accessToken);
        if (jwtTokenProvider.getRemainExpiration(resolveAccessToken) > 0) {
            saveRedis(accessToken, "logout", remainExpiration, TimeUnit.MILLISECONDS);
//            redisTemplate.opsForValue()
//                    .set(accessToken, "logout", remainExpiration, TimeUnit.MILLISECONDS);
        }

        return new ResponseData(Status.LOGOUT_SUCCESS, Status.LOGOUT_SUCCESS.getDescription());
    }

    private void saveRedis(Object key, Object value, Long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue()
                .set(key, value, timeout, timeUnit);
    }

    private void deleteRedis(Object key) {
        redisTemplate.delete(key);
    }

}
