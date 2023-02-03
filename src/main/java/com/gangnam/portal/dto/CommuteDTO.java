package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class CommuteDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteStartEndDTO {
        @Schema(description = "출퇴근 시간")
        @NotNull(message = "출퇴근 시간이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date date; // start, end
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteUpdateDTO {
        @Schema(description = "출퇴근 ID")
        @NotNull(message = "출퇴근 ID가 없습니다.")
        private Long commuteId;
        @Schema(description = "출근 날짜 + 시간")
        @NotNull(message = "출근 시간이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startDate;
        @Schema(description = "퇴근 날짜 + 시간")
        @NotNull(message = "퇴근 시간이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteRegisterDTO {
        @Schema(description = "사원 ID")
        @NotNull(message = "사원 ID가 없습니다.")
        private Long employeeId;
        @Schema(description = "등록 날짜")
        @NotNull(message = "등록일이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private Date registerDate;
        @Schema(description = "출근 날짜 + 시간")
        @NotNull(message = "출근 시간이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date startDate;
        @Schema(description = "퇴근 날짜 + 시간")
        @NotNull(message = "퇴근 시간이 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private Date endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommuteBoardData {
        @Parameter(description = "구분(all or my) : default Value(all)")
        private String range;

        @Parameter(description = "년도", required = true)
        @NotNull(message = "년도가 없습니다.")
//        @Pattern(regexp = "^\\d{4}$", message = "올바른 형식이 아닙니다.")
        private Integer year;

        @Parameter(description = "월", required = true)
        @NotNull(message = "월이 없습니다.")
//        @Pattern(regexp = "^(0?[1-9]|1[012])$", message = "올바른 형식이 아닙니다.")
        private Integer month;
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
        private Integer totalCount;
        private List<CommuteStateData> commuteStateDataList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommuteStateData {
        private Long commuteId;
        private String day;
        private String registerDate;
        private String startDate;
        private String endDate;
        private String nameKr;
        private String holidayName;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CommuteExcelData {
        private Long employeeNo;
        private String nameKr;
        private String registerDate;
        private String dayOfTheWeek;
        private String startDate;
        private String endDate;
        private Double totalCommuteTime;
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
