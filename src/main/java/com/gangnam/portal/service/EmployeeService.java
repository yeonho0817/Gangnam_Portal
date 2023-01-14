package com.gangnam.portal.service;


import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.AuthenticationDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import com.gangnam.portal.util.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final JwtTokenProvider jwtTokenProvider;

    private final EmployeeRepository employeeRepository;
    private final EmployeeCustomRepository employeeCustomRepository;

    // 회원 조회
    public ResponseData findEmployeeInfo(UsernamePasswordAuthenticationToken authenticationToken) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Optional<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo = employeeCustomRepository.findEmployeeInfo(authenticationDTO.getId());

//        if (findEmployeeInfo.isEmpty()) {
//            return new ResponseData(Status.NOT_FOUND_EMPLOYEE, Status.NOT_FOUND_EMPLOYEE.getDescription());
//        } else {
            findEmployeeInfo.get().setEmail(authenticationDTO.getEmail());
            findEmployeeInfo.get().setRole(authenticationDTO.getRole());
            findEmployeeInfo.get().setGender( (findEmployeeInfo.get().getGen()%2 == 0 ? "여자" : "남자") );

            return new ResponseData(Status.FIND_EMPLOYEE_SUCCESS, Status.FIND_EMPLOYEE_SUCCESS.getDescription(), findEmployeeInfo.get());
//        }

    }

    // 회원 수정
    public ResponseData updateEmployeeInfo(UsernamePasswordAuthenticationToken authenticationToken, EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Optional<Employee> findEmployee = employeeRepository.findById(updateInfoDTO.getEmployeeId());

//        if (findEmployee.isEmpty()) {
//            return new ResponseData(Status.NOT_FOUND_EMPLOYEE, Status.NOT_FOUND_EMPLOYEE.getDescription());
//        } else {
            employeeRepository.updateEmployeeInfo(updateInfoDTO.getEmployeeId(),
                    updateInfoDTO.getNameKr()==null ? findEmployee.get().getNameKr() : updateInfoDTO.getNameKr(),
                    updateInfoDTO.getNameEn()==null ? findEmployee.get().getNameEn() : updateInfoDTO.getNameEn(),
                    updateInfoDTO.getPhone()==null ? findEmployee.get().getPhone() : updateInfoDTO.getPhone(),
                    updateInfoDTO.getAddress()==null ? findEmployee.get().getAddress() : updateInfoDTO.getAddress()
            );

            return new ResponseData(Status.UPDATE_EMPLOYEE_INFO_SUCCESS, Status.UPDATE_EMPLOYEE_INFO_SUCCESS.getDescription());
//        }
    }

    // 출퇴근 수정 -> 직원 목록
    public ResponseData readEmployeeNameList() {
        List<EmployeeDTO.EmployeeNameList> employeeNameList = employeeCustomRepository.readEmployeeNameList();

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), employeeNameList);
    }

}
