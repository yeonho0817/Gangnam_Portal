package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class TeamDTO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AffiliationNameDTO {
        private Long affiliationId;
        private String affiliationName;

        private List<DepartmentNameDTO> departmentNameList;

    }

    @Data
    @NoArgsConstructor
    @Builder
    public static class DepartmentNameDTO {
        private Long departmentId;
        private String departmentName;

        private Long affiliationId;
        private String affiliationName;

        public DepartmentNameDTO(Long departmentId, String departmentName, Long affiliationId, String affiliationName) {
            this.departmentId = departmentId;
            this.departmentName = departmentName.equals("UIUX") ? "UI/UX" : departmentName;
            this.affiliationId = affiliationId;
            this.affiliationName = affiliationName;
        }
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RankDTO {
        private Long rankId;
        private String rankName;
    }

}
