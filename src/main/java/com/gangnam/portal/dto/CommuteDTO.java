package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class CommuteDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CommuteRegisterDTO {
        private Date date;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CommuteUpdateDTO {
        private Long employeeId;
        private Date registerDate;
        private Date startDate;
        private Date endDate;
    }
}
