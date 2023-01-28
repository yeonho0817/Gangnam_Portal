package com.gangnam.portal.controller;

import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.WeatherDTO;
import com.gangnam.portal.service.EtcService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
public class EtcController {
    private final EtcService etcService;

    // 날씨 api
    @RequestMapping("/weather")
    public ResponseData<WeatherDTO.WeatherInfo> weatherInfo(@ModelAttribute WeatherDTO.RegionCoordinateDTO regionCoordinate) {
        return etcService.weatherInfo(regionCoordinate);
    }
}
