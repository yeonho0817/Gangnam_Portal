package com.gangnam.portal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "fortune_employee")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class FortuneEmployee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fortune_id")
    private Fortune fortune;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "register_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date registerDate;
}
