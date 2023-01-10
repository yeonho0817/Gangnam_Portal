package com.gangnam.portal.service;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.domain.Provider;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import com.gangnam.portal.repository.redis.RedisRepository;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import com.gangnam.portal.util.jwt.redis.RefreshToken;
import com.gangnam.portal.util.loginApi.googleApi.GoogleLoginInfo;
import com.gangnam.portal.util.loginApi.kakaoApi.KaKaoLoginInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final GoogleLoginInfo googleLoginInfo;
    private final KaKaoLoginInfo kaKaoLoginInfo;

    private final JwtTokenProvider jwtTokenProvider;

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
            return new ResponseData(Status.PROVIDER_REJECTED);
        } else {
            return new ResponseData(Status.PROVIDER_ACCEPTED, responseEntity);
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
            return new ResponseData(Status.LOGIN_FAILED);
        } else {
            // jwt 토큰 생성
            String accessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmail(), isExists.get().getProvider().toString());
            String refreshToken = jwtTokenProvider.generateRefreshToken();

            redisRepository.save(new RefreshToken(refreshToken, isExists.get().getEmail(), jwtTokenProvider.getExpiration(jwtTokenProvider.getResolveToken(refreshToken)).getTime()));

            return new ResponseData(Status.LOGIN_SUCCESS, new EmployeeDTO.LoginResponseDTO(accessToken, refreshToken));
        }

    }


    // 토큰 재발급
    @Transactional
    public ResponseData reissueToken(String accessToken, String refreshToken) {
        // filter에서 정상 처리 완료
        // refresh 기존 꺼 삭제
        // access, refresh 토큰 재발급

        String email = jwtTokenProvider.getEmail(accessToken);
        String provider = jwtTokenProvider.getProvider(accessToken);


        Optional<EmployeeEmail> isExists = employeeEmailCustomRepository.isExists(email, provider);

        // 기존 Refresh 삭제
//        Optional<RefreshToken> isTokenExists = refreshTokenRepository.findByRefreshToken(refreshToken);

//        if (isTokenExists.isPresent()) refreshTokenRepository.deleteByRefreshToken(refreshToken);

        // 새로운 jwt
        // 새로운 refresh token
        String issueAccessToken = jwtTokenProvider.generateAccessToken(isExists.get().getEmail(), isExists.get().getEmployee().getNameKr());
        String issueRefreshToken = jwtTokenProvider.generateRefreshToken();

        // refresh -> redis에 저장
        redisRepository.save(new RefreshToken(refreshToken, isExists.get().getEmail(), jwtTokenProvider.getExpiration(jwtTokenProvider.getResolveToken(refreshToken)).getTime()));

        return new ResponseData(Status.LOGIN_SUCCESS, new EmployeeDTO.LoginResponseDTO(issueAccessToken, issueRefreshToken));
    }


    // 로그아웃
    public ResponseData logout(String accessToken, String refreshToken) {
        // 기존 Refresh 삭제
//        Optional<RefreshToken> isTokenExists = refreshTokenRepository.findByRefreshToken(refreshToken);
//        if (isTokenExists.isPresent()) refreshTokenRepository.deleteByRefreshToken(refreshToken);


        accessToken = jwtTokenProvider.getResolveToken(accessToken);

        // redis에 access Token 등록
        Long remainExpiration = jwtTokenProvider.getRemainExpiration(accessToken);
        if (jwtTokenProvider.getRemainExpiration(accessToken) > 0) {
            redisRepository.save(new RefreshToken(accessToken, jwtTokenProvider.getEmail(accessToken), remainExpiration));
        }

        return new ResponseData(Status.LOGOUT_SUCCESS);
    }
}
