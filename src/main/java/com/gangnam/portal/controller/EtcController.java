package com.gangnam.portal.controller;

import com.gangnam.portal.dto.FortuneDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.WeatherDTO;
import com.gangnam.portal.service.EtcService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
public class EtcController {
    private final EtcService etcService;

    // 날씨 api
    @GetMapping("/weather")
    public ResponseData<WeatherDTO.WeatherInfo> weatherInfo(@ModelAttribute WeatherDTO.RegionCoordinateDTO regionCoordinate) {
        return etcService.weatherInfo(regionCoordinate);
    }

    // 운세 api
    @GetMapping("/fortune")
    public ResponseData<FortuneDTO> fortuneInfo(UsernamePasswordAuthenticationToken authentication) {
        return etcService.fortuneInfo(authentication);
    }

}
