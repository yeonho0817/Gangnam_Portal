package com.gangnam.portal.dto;

import com.gangnam.portal.domain.AffiliationName;
import com.gangnam.portal.domain.DepartmentName;
import com.gangnam.portal.domain.RankName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EmployeeDTO {

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfoDTO {
        /*

        @NotBlank 는 null 과 "" 과 " "
        @NotEmpty 는 null 과 "" 둘 다 허용하지 않게 합니다.
        @NotNull 은 위에 살펴본 것 처럼 이름 그대로 Null만 허용하지 않습니다. 따라서, "" 이나 " " 은 허용하게 됩니다.

         */

        @NotNull(message = "직원 ID가 없습니다.")
        private Long employeeId;
        private String nameKr;
        private String nameEn;
        private String phone;
        private String address;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class EmployeeInfoDTO {
        private Long employeeId;
        private Long employeeNo;
        private String nameKr;
        private String nameEn;
        private String birthday;
        private RankName rank;
        private AffiliationName affiliation;
        private DepartmentName department;
        private String email;
        private String profileImg;
        private Integer gen;
        private String gender;
        private String phone;
        private String address;
        private String joinDate;

        private String role;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class EmployeeNameList {
        private Long employeeId;
        private String nameKr;
    }

//    @Data
//    @RequiredArgsConstructor
//    @AllArgsConstructor
//    public static class TokenDTO {
//        private String accessToken;
//        private String refreshToken;
//        private String role;
//    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRInfo {
        private Integer totalPage;
        List<HRInfoData> hrInfoData;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRInfoData {
        private Long employeeId;
        private String nameKr;
        private String rank;
        private String affiliation;
        private String department;
        private String email;
        private String phone;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRDepartmentInfo {
        private Integer totalPage;
        private List<HRDepartmentInfoData> hrDepartmentInfoDataList;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRDepartmentInfoData {
        private Long employeeId;
        private String nameKr;
        private String rank;
        private String affiliation;
        private String department;
    }

}
