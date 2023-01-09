package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public class EmployeeDTO {

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String email;
        private String password;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public class FindPasswordDTO {
        private String email;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public class UpdateInfoDTO {
        private String password;
        private String phone;
        private String address;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public class ReadHumanResource {
        private Long employeeId;
        private String name;
        private String ranks;
        private String department;
        private String team;
        private String email;
        private String phone;
    }
}
