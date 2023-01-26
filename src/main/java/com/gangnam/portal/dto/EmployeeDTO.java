package com.gangnam.portal.dto;

import com.gangnam.portal.domain.AffiliationName;
import com.gangnam.portal.domain.DepartmentName;
import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.domain.RankName;
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
        private String state;


        private String role;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class EmployeeNameList {
        private Long employeeId;
        private String nameKr;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class HRInfo {
        private Integer totalPage;
        private Integer totalCount;
        List<HRInfoDataList> hrInfoDataLists;
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
        private List<EmployeeEmail> email;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class HRInfoDataList {
        private Long employeeId;
        private String nameKr;
        private String rank;
        private String affiliation;
        private String department;
        private String phone;
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
