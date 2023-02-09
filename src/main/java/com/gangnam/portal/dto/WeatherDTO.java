package com.gangnam.portal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDTO {
    @JsonProperty("response")
    public Response response;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        @JsonProperty("body")
        public Body body;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Body {
        @JsonProperty("items")
        public Items items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Items {
        @JsonProperty("item")
        public List<Item> item;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @JsonProperty("fcstValue")
        public String fcstvalue;
        @JsonProperty("fcstTime")
        public String fcsttime;
//        @JsonProperty("fcstDate")
//        public String fcstdate;
        @JsonProperty("category")
        public String category;
//        @JsonProperty("baseTime")
//        public String basetime;
//        @JsonProperty("baseDate")
//        public String basedate;
    }
}
