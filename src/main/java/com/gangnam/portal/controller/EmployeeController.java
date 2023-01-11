package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    // 회원 조회
    @GetMapping("/hr/info")
    public ResponseEntity findEmployeeInfo(UsernamePasswordAuthenticationToken authentication) {
        ResponseData responseData = employeeService.findEmployeeInfo(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
    
    // 회원 수정
    @PutMapping("/hr/info/update")
    public ResponseEntity updateEmployeeInfo(@RequestBody @Valid EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        ResponseData responseData = employeeService.updateEmployeeInfo(updateInfoDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 출퇴근 수정 시 직원 목록
    @GetMapping("/hr/list")
    public ResponseEntity readEmployeeNameList() {
        ResponseData responseData = employeeService.readEmployeeNameList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);

    }
}
