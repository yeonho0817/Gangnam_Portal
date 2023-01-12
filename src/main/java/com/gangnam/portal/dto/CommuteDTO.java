package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class CommuteDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteRegisterDTO {
//        @NotBlank(message = "직원 ID가 없습니다.")
//        private Long employeeId;
        @NotBlank(message = "날짜가 없습니다.")
        @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date date; // start, end
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteUpdateDTO {
        private Long commuteId;
        @NotBlank(message = "직원 ID가 없습니다.")
        private Long employeeId;
        @NotBlank(message = "날짜가 없습니다.")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "날짜 형식이 아닙니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private Date registerDate;
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startDate;
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteDateInfo {
        @NotNull(message = "년도를 입력해주세요")
        @Pattern(regexp = "^\\d{4}$", message = "년도가 유효하지 않습니다.")
        private String year;

        @NotNull(message = "월을 입력해주세요")
        @Pattern(regexp = "^(0?[1-9]|1[012])$", message = "월이 유효하지 않습니다.")
        private String month;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteListBoard {
        private String name;
        private Date registerDate;
        private String day;
        private Date startDate;
        private Date endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteState {
        private String registerDate;
        private String startDate;
        private String endDate;
        private String nameKr;
    }




}
