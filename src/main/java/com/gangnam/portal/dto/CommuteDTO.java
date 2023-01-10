package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class CommuteDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CommuteRegisterDTO {
        @NotBlank(message = "직원 ID가 없습니다.")
        private Long employeeId;
        @NotBlank(message = "날짜가 없습니다.")
        @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date date; // start, end
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
