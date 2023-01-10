package com.gangnam.portal.service;

import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.repository.CommuteRepository;
import com.gangnam.portal.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommuteService {
    private final EmployeeRepository employeeRepository;
    private final CommuteRepository commuteRepository;

    // 출근 등록
    public ResponseData commuteStart(CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
//        Optional<Employee> findEmployee = employeeRepository.findById(commuteRegisterDTO)
//
//        Commute newCommute = Commute.builder()


        return null;
    }

    // 퇴근 등록
    public void commuteEnd() {

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
