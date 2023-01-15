package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class TeamDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AffiliationNameDTO {
        private Long affiliationId;
        private String affiliationName;

        private List<DepartmentNameDTO> departmentNameDTOList;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentNameDTO {
        private Long departmentId;
        private String departmentName;
    }


}
