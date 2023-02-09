package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDTO {
    @JsonProperty("response")
    private HolidayResponse holidayResponse;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HolidayResponse {
        @JsonProperty("body")
        private HolidayBody holidayBody;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HolidayBody {
        private HolidayItems holidayItems;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HolidayItems {
        @JsonProperty("item")
        private List<HolidayItem> holidayItem;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HolidayItem {
        @JsonProperty("locdate")
        private String locdate;
        @JsonProperty("isHoliday")
        private String isholiday;
        @JsonProperty("dateName")
        private String datename;
    }

}
