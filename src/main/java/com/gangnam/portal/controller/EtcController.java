package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EtcDTO;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.EtcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(operationId = "weather", summary = "날씨 정보 조회", description = "오늘 날씨 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "날씨 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "5XX", description = "날씨 정보 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EtcDTO.WeatherInfo> weatherInfo(@ModelAttribute EtcDTO.RegionCoordinateDTO regionCoordinate) {
        return etcService.weatherInfo(regionCoordinate);
    }

    // 운세 api
    @GetMapping("/fortune")
    @Operation(operationId = "weather", summary = "운세 정보 조회", description = "오늘 운세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운세 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "운세 정보 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "5XX", description = "운세 정보 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EtcDTO.FortuneDTO> fortuneInfo(UsernamePasswordAuthenticationToken authentication) {
        return etcService.fortuneInfo(authentication);
    }

}
