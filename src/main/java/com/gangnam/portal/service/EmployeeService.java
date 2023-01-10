package com.gangnam.portal.service;


import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;

    // 회원 조회
    public ResponseData findEmployeeInfo(Long id) {



        return null;
    }

    // 회원 수정
    public ResponseData updateEmployeeInfo(EmployeeDTO.UpdateInfoDTO updateInfoDTO) {

        return null;
    }
}
