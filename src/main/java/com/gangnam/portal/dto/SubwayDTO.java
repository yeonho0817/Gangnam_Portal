package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubwayDTO {
    @JsonProperty("realtimeArrivalList")
    private List<Realtimearrivallist> realtimearrivallist;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Realtimearrivallist {
        @JsonProperty("arvlMsg3")
        private String arvlmsg3;
        @JsonProperty("arvlMsg2")
        private String arvlmsg2;
        @JsonProperty("trainLineNm")
        private String trainlinenm;
    }
}
