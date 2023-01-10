package com.gangnam.portal.service;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.CommuteCustomRepository;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommuteService {
    private final JwtTokenProvider jwtTokenProvider;

    private final EmployeeRepository employeeRepository;
    private final CommuteCustomRepository commuteCustomRepository;

    // 출근 등록
    public ResponseData commuteStart(String accessToken, CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        Optional<Employee> findEmployee = employeeRepository.findById(jwtTokenProvider.getId(jwtTokenProvider.getResolveToken(accessToken)));
        if (findEmployee.isEmpty()) return new ResponseData(Status.NOT_FOUND_EMAIL);

        Commute newCommute = Commute.builder()
                .employee(findEmployee.get())
                .startDate(commuteRegisterDTO.getDate())
                .build();

        return new ResponseData(Status.COMMUTE_START_SUCCESS);
    }

    // 퇴근 등록
    public ResponseData commuteEnd(String accessToken, CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        Optional<Employee> findEmployee = employeeRepository.findById(jwtTokenProvider.getId(jwtTokenProvider.getResolveToken(accessToken)));
        if (findEmployee.isEmpty()) return new ResponseData(Status.NOT_FOUND_EMAIL);

//        Optional<Commute> findCommute = commuteCustomRepository.findCommute();

        return new ResponseData(Status.COMMUTE_END_SUCCESS);
    }

    // 출퇴근 수정
    public void commuteUpdate() {

    }

    // 월별 출퇴근 조회 - 내꺼, 전체꺼
    public void commute() {

    }

    // 출퇴근 현황 조회
    public void commuteState() {

    }
}
