package com.gangnam.portal.service;

import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.domain.Fortune;
import com.gangnam.portal.domain.FortuneEmployee;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.EtcDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.FortuneEmployeeRepository;
import com.gangnam.portal.repository.FortuneRepository;
import com.gangnam.portal.repository.custom.FortuneEmployeeCustomRepository;
import com.gangnam.portal.util.wheaterApi.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EtcService {
    private final EmployeeRepository employeeRepository;
    private final FortuneEmployeeCustomRepository fortuneEmployeeCustomRepository;
    private final FortuneRepository fortuneRepository;
    private final FortuneEmployeeRepository fortuneEmployeeRepository;
    private final Weather weather;

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

        // 이미 있는지 검사
        Optional<EtcDTO.FortuneDTO> findFortuneMessage = fortuneEmployeeCustomRepository.findByEmployeeId(findEmployee.getId());
        if (findFortuneMessage.isPresent()) return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), findFortuneMessage.get());

        // radom 숫자
        Long randomNumber = (long) ((int) (Math.random() * 507) + 1);

        Fortune findFortune = fortuneRepository.findById(randomNumber)
                .orElseThrow(() -> new CustomException(ErrorStatus.FORTUNE_INFO_FAIL));

        FortuneEmployee newFortuneEmployee = FortuneEmployee.builder()
                .employee(findEmployee)
                .fortune(findFortune)
                .registerDate(new Date())
                .build();

        fortuneEmployeeRepository.save(newFortuneEmployee);

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EtcDTO.FortuneDTO(false, findFortune.getMessage()));
    }
    
    // fortune employee 삭제 - 스케쥴러
    @Transactional(rollbackFor = {Exception.class})
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void deleteTodayFortune() {
        fortuneEmployeeRepository.truncate();
    }
}
