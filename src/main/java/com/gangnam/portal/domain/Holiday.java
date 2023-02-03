package com.gangnam.portal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holiday")
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Holiday {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_name")
    private String dateName;

    @Temporal(TemporalType.DATE)
    @Column(name = "holiday_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date holidayDate;
}
