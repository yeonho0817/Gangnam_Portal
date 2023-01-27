package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class EmployeeDTO {

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfoDTO {
        private String nameEn;
        private String phone;
        private String address;
    }

//    @Data
//    @RequiredArgsConstructor
//    @AllArgsConstructor
//    public static class EmployeeInfoDTO {
//        private Long employeeId;
//        private Long employeeNo;
//        private String nameKr;
//        private String nameEn;
//        private String birthday;
//        private RankName rank;
//        private AffiliationName affiliation;
//        private DepartmentName department;
//
//        private String profileImg;
//        private Integer gen;
//        private String gender;
//        private String phone;
//        private String address;
//        private String joinDate;
//        private String state;
//
//        private String email;
//
//        private String role;
//    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmployeeInfoDTO {
        private Long employeeId;
        private Long employeeNo;
        private String role;
        private String nameKr;
        private String nameEn;
        private String birthday;
        private String rank;
        private String affiliation;
        private String department;

        private String profileImg;
        private String gender;
        private String phone;
        private String address;
        private String joinDate;
        private String state;

        private String email;

    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmployeeNameList {
        private Long employeeId;
        private Long employeeNo;
        private String name;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRInfo {
        private Integer totalPage;
        private Integer totalCount;
        List<HRInfoData> hrInfoDataLists;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class HRInfoData {
        private Long employeeId;
        private String nameKr;
        private String rank;
        private String affiliation;
        private String department;
        private String phone;
        private List<String> email;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class Test {
        private Long employeeId;
        private String nameKr;
        private String rank;
        private List<EmailInfo> email;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class EmailInfo {
        private String email;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRDepartmentInfo {
        private Integer totalPage;
        private Integer totalCount;
        private List<EmployeeSimpleInfo> hrDepartmentInfoDataList;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class EmployeeSimpleInfo {
        private Long employeeId;
        private Long employeeNo;
        private String nameKr;
        private String rank;
        private String affiliation;
        private String department;
    }



}
