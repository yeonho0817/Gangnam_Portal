package com.gangnam.portal.service;


import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeCustomRepository employeeCustomRepository;

    // 회원 조회
    @Transactional(readOnly = true)
    public ResponseData<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(UsernamePasswordAuthenticationToken authenticationToken) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        EmployeeDTO.EmployeeInfoDTO findEmployeeInfo = employeeCustomRepository.findEmployeeInfo(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        findEmployeeInfo.setEmail(authenticationDTO.getEmail());
        findEmployeeInfo.setRole(authenticationDTO.getRole());
        findEmployeeInfo.setGender( (findEmployeeInfo.getGen()%2 == 0 ? "여자" : "남자") );

        return new ResponseData<>(Status.FIND_EMPLOYEE_SUCCESS, Status.FIND_EMPLOYEE_SUCCESS.getDescription(), findEmployeeInfo);
    }

    // 회원 수정    O
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData updateEmployeeInfo(UsernamePasswordAuthenticationToken authenticationToken, EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Employee findEmployee = employeeRepository.findById(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));;

        findEmployee.updateNameEn(updateInfoDTO.getNameEn());
        findEmployee.updatePhone(updateInfoDTO.getPhone());
        findEmployee.updateAddress(updateInfoDTO.getAddress());

        return new ResponseData(Status.UPDATE_EMPLOYEE_INFO_SUCCESS, Status.UPDATE_EMPLOYEE_INFO_SUCCESS.getDescription());

    }

    // 출퇴근 수정 -> 직원 목록
    @Transactional(readOnly = true)
    public ResponseData<List<EmployeeDTO.EmployeeNameList>> readEmployeeNameList() {
        List<EmployeeDTO.EmployeeNameList> employeeNameList = employeeCustomRepository.readEmployeeNameList();

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), employeeNameList);
    }

}
