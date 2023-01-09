package com.gangnam.portal.service;

import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.util.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    //로그인
    public ResponseData login(EmployeeDTO.LoginDTO loginDTO) {
        Optional<Employee> findEmployee = employeeRepository.findByEmail(loginDTO.getEmail());

        // 불일치 시
        if (findEmployee.isEmpty()) return new ResponseData(Status.EMAIL_NOT_FOUND);
        if (! passwordEncoder.matches(loginDTO.getPassword(), findEmployee.get().getPassword())) return new ResponseData(Status.PASSWORD_NOT_FOUND);
        
        // 일치 시
        String accessToken = jwtTokenUtil.generateAccessToken(findEmployee.get().getId(), findEmployee.get().getEmail());
        String refreshToken = jwtTokenUtil.generateRefreshToken(findEmployee.get().getId(), findEmployee.get().getEmail());
        

        return null;
    }

    // 로그아웃
    public void logout() {

    }

    // PW 찾기
    public void findPassword() {

    }

    // 회원 조회
    public void findEmployeeInfo() {

    }

    // 회원 수정
    public void findEmployeeInfoUpdate() {

    }
}
