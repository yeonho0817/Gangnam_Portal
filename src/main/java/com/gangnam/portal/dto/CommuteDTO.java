package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

public class CommuteDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteStartEndDTO {
//        @NotBlank(message = "직원 ID가 없습니다.")
//        private Long employeeId;
//        @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @Schema(description = "출퇴근 시간")
        @NotNull(message = "필수 값이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date date; // start, end
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteUpdateDTO {
        @Schema(description = "출퇴근 ID")
        @NotNull(message = "필수 값이 없습니다.")
        private Long commuteId;
//        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @Schema(description = "출근 날짜 + 시간")
        @NotNull(message = "필수 값이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startDate;
//        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @Schema(description = "퇴근 날짜 + 시간")
        @NotNull(message = "필수 값이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteRegisterDTO {
        @Schema(description = "사원 ID")
        @NotNull(message = "필수 값이 없습니다.")
        private Long employeeId;
//        @NotBlank(message = "날짜가 없습니다.")
//        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "날짜 형식이 아닙니다.")
        @Schema(description = "등록 날짜")
        @NotNull(message = "필수 값이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private Date registerDate;
//        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @Schema(description = "출근 날짜 + 시간")
        @NotNull(message = "필수 값이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startDate;
//        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])$", message = "날짜 형식이 아닙니다.")
        @Schema(description = "퇴근 날짜 + 시간")
        @NotNull(message = "필수 값이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteDateInfo {
        @Parameter(description = "년도", required = true)
        @NotNull(message = "필수 값이 없습니다.")
        @Pattern(regexp = "^\\d{4}$", message = "올바른 형식이 아닙니다.")
        private String year;

        @Parameter(description = "월", required = true)
        @NotNull(message = "필수 값이 없습니다.")
        @Pattern(regexp = "^(0?[1-9]|1[012])$", message = "올바른 형식이 아닙니다.")
        private String month;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteListBoard {
        private Long commuteId;
        private String nameKr;
        private String registerDate;
        private String day;
        private String startDate;
        private String endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteState {
        private Integer totalPages;
        private List<CommuteStateData> commuteStateData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteStateData {
        private Long commuteId;
        private String registerDate;
        private String startDate;
        private String endDate;
        private String nameKr;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteStateCondition {
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private Date startDate;
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private Date endDate;
    }




}
