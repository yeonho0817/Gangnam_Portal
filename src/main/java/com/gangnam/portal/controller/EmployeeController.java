package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    // 회원 조회
    @GetMapping("/hr/info/{id}")
    public ResponseEntity findEmployeeInfo(@PathVariable("id") Long id) {
        ResponseData responseData = employeeService.findEmployeeInfo(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
    
    // 회원 수정
    @PutMapping("/hr/info/update")
    public ResponseEntity updateEmployeeInfo(@RequestBody EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        ResponseData responseData = employeeService.updateEmployeeInfo(updateInfoDTO);


        return null;
    }
}
