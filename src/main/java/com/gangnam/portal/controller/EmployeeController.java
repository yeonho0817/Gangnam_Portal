package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class EmployeeController {
    private final EmployeeService employeeService;

    //로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody EmployeeDTO.LoginDTO loginDTO) {

        ResponseData responseData = employeeService.login(loginDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
    
    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity logout() {

        return null;
    }
    
    // PW 찾기
    @GetMapping("/hr/password")
    public ResponseEntity findPassword(@ModelAttribute EmployeeDTO.FindPasswordDTO findPasswordDTO) {

        return null;
    }
    
    // 회원 조회
    @GetMapping("/hr/info")
    public ResponseEntity findEmployeeInfo(@PathVariable("id") Long id) {

        return null;
    }
    
    // 회원 수정
    @PutMapping("/hr/info/update")
    public ResponseEntity findEmployeeInfoUpdate(@RequestBody EmployeeDTO.UpdateInfoDTO updateInfoDTO) {

        return null;
    }
}
