package com.gangnam.portal.service;

import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.dto.WeatherDTO;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.util.wheaterApi.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EtcService {
    private final Weather weather;

    // 날씨 api
    public ResponseData<WeatherDTO.WeatherInfo> weatherInfo(WeatherDTO.RegionCoordinateDTO regionCoordinate) {
        WeatherDTO.WeatherInfo weatherInfo = null;

        try {
            WeatherDTO.RegionCodeDTO regionCodeDTO = weather.regionCodeInfo(regionCoordinate);

            weatherInfo = weather.lookUpWeather(regionCodeDTO);
        } catch(IOException | NullPointerException e) {
            throw new CustomException(ErrorStatus.WEATHER_INFO_FAIL);
        }

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), weatherInfo);
    }
}
