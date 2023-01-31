package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EtcDTO;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.service.EtcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
    public ResponseData<EtcDTO.FortuneDTO> fortuneInfo(@ApiIgnore UsernamePasswordAuthenticationToken authentication) {
        return etcService.fortuneInfo(authentication);
    }

    // 지하철 api
    @GetMapping("/subway")
    @Operation(operationId = "weather", summary = "운세 정보 조회", description = "오늘 운세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "운세 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "5XX", description = "운세 정보 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EtcDTO.SubwayInfo> subway() {
        return etcService.subwayInfo();
    }

    @GetMapping("/test")
    @ApiIgnore
    public ResponseData<EtcDTO.SubwayInfo> test(@Parameter Long id) {
        /*Random random = new Random();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String str = formatter.format(new Date());

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, Integer.parseInt(str.substring(0, 4)));
        calendar.set(Calendar.MONTH, Integer.parseInt(str.substring(4, 6))-1);
        calendar.set(Calendar.DATE, Integer.parseInt(str.substring(6, 8)));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        System.out.println(calendar.getTime());

        random.setSeed(id + calendar.getTime().getTime());

        System.out.println(random.nextInt(507));*/

        List<EtcDTO.SubwayInfoData> subwayInfoList = new ArrayList<>();

        EtcDTO.SubwayInfoData subwayInfo1 = EtcDTO.SubwayInfoData.builder()
                    .direction("신논현방면")
                    .time("강남 도착")
                    .currentPosition("강남")
                .build();

        EtcDTO.SubwayInfoData subwayInfo2 = EtcDTO.SubwayInfoData.builder()
                .direction("역삼방면")
                .time("4분 31초 후")
                .currentPosition("서울종합운동장")
                .build();

        EtcDTO.SubwayInfoData subwayInfo3 = EtcDTO.SubwayInfoData.builder()
                .direction("양재방면")
                .time("서울종합운동장(3 전)")
                .currentPosition("서울종합운동장")
                .build();

        EtcDTO.SubwayInfoData subwayInfo4 = EtcDTO.SubwayInfoData.builder()
                .direction("교대방면")
                .time("판교(4 전)")
                .currentPosition("판교")
                .build();

        subwayInfoList.add(subwayInfo1);
        subwayInfoList.add(subwayInfo2);
        subwayInfoList.add(subwayInfo3);
        subwayInfoList.add(subwayInfo4);


        subwayInfoList.sort(Comparator.comparing(EtcDTO.SubwayInfoData::getDirection));

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EtcDTO.SubwayInfo(formatter.format(new Date()), subwayInfoList));

    }

}
