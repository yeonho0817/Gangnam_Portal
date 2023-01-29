package com.gangnam.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "fortune")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Fortune {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", columnDefinition = "VARCHAR(500)")
    private String message;
}
