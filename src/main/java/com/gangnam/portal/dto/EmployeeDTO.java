package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class EmployeeDTO {

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfoDTO {
        private String password;
        private String phone;
        private String address;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class EmployeeInfoDTO {
        private Long employeeId;
        private Long employeeNo;
        private String name_kr;
        private String name_en;
        private String birthday;
        private String ranks;
        private String affiliation;
        private String department;
        private String email;
        private String profileImg;
        private String gender;
        private String phone;
        private String address;
        private String joinDate;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDTO {
        private String accessToken;
        private String refreshToken;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class FindEmployeeInfoDTO {
        private String email;
        private String roles;
    }

}
