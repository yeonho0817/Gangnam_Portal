package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

public class EmployeeDTO {

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class UpdateInfoDTO {
        @NotBlank(message = "영어 이름이 빈칸입니다.")
        private String nameEn;
        @NotBlank(message = "휴대폰 번호가 빈칸입니다.")
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$",message = "핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")
        private String phone;
        @NotBlank(message = "주소가 빈칸입니다.")
        private String address;
//        @NotBlank(message = "프로필이 빈칸입니다.")
        private MultipartFile profileImg;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmployeeAdminInfo {
        private Long employeeId;
        @NotNull(message = "사번이 빈칸입니다.")
        private Long employeeNo;
        @NotNull(message = "역할이 빈칸입니다.")
        private Long roleId;
        @NotBlank(message = "한글 이름이 빈칸입니다.")
        private String nameKr;
        @NotBlank(message = "영어 이름이 빈칸입니다.")
        private String nameEn;
        @NotNull(message = "생일이 빈칸입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//        @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 날짜 형식이 아닙니다.")
        private Date birthday;
        @NotNull(message = "직급이 빈칸입니다.")
        private Long rankId;
        @NotNull(message = "소속이 빈칸입니다.")
        private Long affiliationId;
        @NotNull(message = "부서가 빈칸입니다.")
        private Long departmentId;

//        private MultipartFile profileImg;
        @NotBlank(message = "성별이 빈칸입니다.")
        private String gender;
        @NotBlank(message = "전화번호가 빈칸입니다.")
        private String phone;
        @NotBlank(message = "주소가 빈칸입니다.")
        private String address;
        @NotNull(message = "입사일 빈칸입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
//        @Pattern(regexp = "\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "올바른 날짜 형식이 아닙니다.")
        private Date joinDate;
        @NotNull(message = "입사 상태가 빈칸입니다.")
        private String state;

        @NotBlank(message = "구글 이메일이 빈칸입니다.")
//        @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@twolinecode.com$", message = "회사 이메일 형식에 맞지 않습니다.")
        private String googleEmail;
//        @Pattern(regexp = "^(\\s)|([0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3})$" ,message = "이메일 형식이 맞지 않습니다.")
        private String kakaoEmail;
    }


    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmployeeInfoDTO {
        private Long employeeId;
        private Long employeeNo;
        private Long roleId;
        private String role;
        private String nameKr;
        private String nameEn;
        private String birthday;
        private Long rankId;
        private String rank;
        private Long affiliationId;
        private String affiliation;
        private Long departmentId;
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
