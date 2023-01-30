package com.gangnam.portal.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class EtcDTO {
    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RegionCoordinateDTO {
        @Parameter(description = "위도")
        private Double latitude;
        @Parameter(description = "경도")
        private Double longitude;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class RegionCodeDTO {
        private Integer x;
        private Integer y;
    }

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WeatherInfo {
        @Parameter(description = "기준 날짜")
        private String date;
        @Parameter(description = "기준 시간(hour)")
        private String time;
        @Parameter(description = "낮 or 밤")
        private Boolean isNight;
        @Parameter(description = "기온(℃)")
        private String tmp;
        @Parameter(description = "풍속(m/s)")
        private String wsd;
        @Parameter(description = "강수 확률(%)")
        private String pop;
        @Parameter(description = "강수량(mm)")
        private String pcp;
        @Parameter(description = "강수 형태(없음, 비, 비/눈, 눈, 소나기)")
        private String pty;
        @Parameter(description = "하늘 상태(맑음, 구름많음, 흐림)")
        private String sky;
        @Parameter(description = "신적설(cm)")
        private String sno;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class FortuneDTO {
        private String message;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static class SubwayInfoData {
        private String direction;
        private String time;
        private String currentPosition;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static class SubwayInfo {
        private String baseDateTime;
        private List<SubwayInfoData> subwayInfo;
    }

}
