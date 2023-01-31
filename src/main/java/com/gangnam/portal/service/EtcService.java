package com.gangnam.portal.service;

import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.domain.Fortune;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.EtcDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.FortuneRepository;
import com.gangnam.portal.util.subwayApi.Subway;
import com.gangnam.portal.util.wheaterApi.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EtcService {
    private final EmployeeRepository employeeRepository;
//    private final FortuneEmployeeCustomRepository fortuneEmployeeCustomRepository;
    private final FortuneRepository fortuneRepository;
//    private final FortuneEmployeeRepository fortuneEmployeeRepository;
    private final Weather weather;
    private final Subway subway;

    // 날씨 api
    @Transactional(readOnly = true)
    public ResponseData<EtcDTO.WeatherInfo> weatherInfo(EtcDTO.RegionCoordinateDTO regionCoordinate) {
        EtcDTO.WeatherInfo weatherInfo = null;

        try {
            EtcDTO.RegionCodeDTO regionCodeDTO = weather.regionCodeInfo(regionCoordinate);

            weatherInfo = weather.lookUpWeather(regionCodeDTO);
        } catch(IOException | NullPointerException e) {
            throw new CustomException(ErrorStatus.WEATHER_INFO_FAIL);
        }

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), weatherInfo);
    }

    // 운세 api
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData<EtcDTO.FortuneDTO> fortuneInfo(UsernamePasswordAuthenticationToken authentication) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authentication);

        Employee findEmployee = employeeRepository.findById(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        // radom 숫자
        Random random = new Random();

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

        random.setSeed(authenticationDTO.getId() + calendar.getTime().getTime());

        Long fortuneId = (long) random.nextInt(507);

        System.out.println(fortuneId + " " + authenticationDTO.getId());

        Fortune findFortune = fortuneRepository.findById(fortuneId)
                .orElseThrow(() -> new CustomException(ErrorStatus.FORTUNE_INFO_FAIL));

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EtcDTO.FortuneDTO(findFortune.getMessage()));
    }
    
    // fortune employee 삭제 - 스케쥴러
//    @Transactional(rollbackFor = {Exception.class})
//    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
//    public void deleteTodayFortune() {
//        fortuneEmployeeRepository.truncate();
//    }

    public ResponseData<EtcDTO.SubwayInfo> subwayInfo() {
        List<EtcDTO.SubwayInfoData> subwayInfoList = null;
        try {
            subwayInfoList = subway.subwayInfo();
        } catch (IOException | NullPointerException e) {
            throw new CustomException(ErrorStatus.SUBWAY_INFO_FAIL);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EtcDTO.SubwayInfo(formatter.format(new Date()), subwayInfoList));
    }
}
