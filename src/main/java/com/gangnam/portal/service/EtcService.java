package com.gangnam.portal.service;

import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.domain.Fortune;
import com.gangnam.portal.domain.Holiday;
import com.gangnam.portal.dto.*;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.FortuneRepository;
import com.gangnam.portal.repository.HolidayRepository;
import com.gangnam.portal.util.holidayApi.HolidayApi;
import com.gangnam.portal.util.subwayApi.SubwayApi;
import com.gangnam.portal.util.wheaterApi.WeatherApi;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EtcService {
    private final EmployeeRepository employeeRepository;
    private final HolidayRepository holidayRepository;
    private final FortuneRepository fortuneRepository;
    private final WeatherApi weatherApi;
    private final SubwayApi subwayApi;
    private final HolidayApi holidayApi;

    // 날씨 api
    @Transactional(readOnly = true)
    public ResponseData<EtcDTO.WeatherInfo> weatherInfo(EtcDTO.RegionCoordinateDTO regionCoordinate) {
        EtcDTO.WeatherInfo weatherInfo = null;

        try {
            regionCoordinate.setLatitude(37.4964091);
            regionCoordinate.setLongitude(127.029695);

            EtcDTO.RegionCodeDTO regionCodeDTO = weatherApi.regionCodeInfo(regionCoordinate);

            weatherInfo = weatherApi.lookUpWeather(regionCodeDTO);
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

    public ResponseData<EtcDTO.SubwayInfo> subwayInfo() {
        List<EtcDTO.SubwayInfoData> subwayInfoList = new ArrayList<>();

        List<SubwayDTO.Realtimearrivallist> realtimearrivallistList = subwayApi.subwayInfo();

        for (SubwayDTO.Realtimearrivallist realtimearrivallist: realtimearrivallistList) {
            String direction = realtimearrivallist.getTrainlinenm().split(" - ")[1];

            String currentPosition = realtimearrivallist.getArvlmsg3();
            String time = realtimearrivallist.getArvlmsg2();

            if (time.contains("번째 전역")) time = currentPosition + "(" + time.split("]번째 전역")[0].substring(1) + " 전)";

            EtcDTO.SubwayInfoData subwayInfo = EtcDTO.SubwayInfoData.builder()
                    .direction(direction)
                    .time(time)
                    .currentPosition(currentPosition)
                    .build();

            subwayInfoList.add(subwayInfo);
        }

        subwayInfoList.sort(Comparator.comparing(EtcDTO.SubwayInfoData::getDirection));

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EtcDTO.SubwayInfo(formatter.format(new Date()), subwayInfoList));
    }

    @Transactional(rollbackFor = {Exception.class})
    @Scheduled(cron = "00 00 23 31 12 *", zone = "Asia/Seoul")
    public void holidayInfo() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            List<HolidayDTO.HolidayItem> holidayItemList = holidayApi.holidayInfo(calendar.get(Calendar.YEAR)+1);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            for (HolidayDTO.HolidayItem holidayItem : holidayItemList) {
                Holiday newHoliday = Holiday.builder()
                        .dateName(holidayItem.getDatename())
                        .holidayDate(formatter.parse(holidayItem.getLocdate().substring(0, 4) + "-" + holidayItem.getLocdate().substring(4, 6) + "-" + holidayItem.getLocdate().substring(6, 8)))

                        .build();

                holidayRepository.save(newHoliday);
            }

        } catch (Exception e) {
            throw new CustomException(ErrorStatus.HOLIDAY_INFO_FAIL);
        }
    }

    public void test() {
        try{
            EtcDTO.RegionCoordinateDTO regionCoordinateDTO = new EtcDTO.RegionCoordinateDTO(37.4964091, 127.029695);

            List<WeatherDTO.Item> itemList = weatherApi.test(regionCoordinateDTO);

            System.out.println(itemList);

        } catch (Exception e) {
            throw new CustomException(ErrorStatus.WEATHER_INFO_FAIL);
        }
    }
}
