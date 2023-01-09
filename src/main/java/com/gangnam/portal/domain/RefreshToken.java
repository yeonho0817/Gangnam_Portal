package com.gangnam.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "refresh_token")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refresh_token", nullable = false, columnDefinition = "VARCHAR(300)")
    private String refreshToken;

    @Column(name = "expiration", nullable = false, columnDefinition = "DATETIME")
    private Date expiration;
}
