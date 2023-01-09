package com.gangnam.portal.repository;


import com.gangnam.portal.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String token);

    void deleteByRefreshToken(String refreshToken);

    void deleteByExpirationLessThanEqual(LocalDateTime expiration);
}
