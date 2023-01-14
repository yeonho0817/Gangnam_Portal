package com.gangnam.portal.controller;

import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.HumanResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hr")
public class HumanResourceController {
    private final HumanResourceService humanResourceService;


    // 소속/부서 이름 조회
    @GetMapping("/teamList")
    public ResponseEntity findAffiliationDepartment() {
        ResponseData responseData = humanResourceService.findAffiliationDepartment();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }


    // 인력 조회
    @GetMapping("/management")
    public ResponseEntity findHumanResource(@RequestParam(defaultValue = "name") String sort,
                                            @RequestParam(defaultValue = "ASC") String orderBy,
                                            @RequestParam(required = false) String selectValue,
                                            @RequestParam(required = false) String searchText,
                                            @RequestParam(required = false, defaultValue = "1") String pageNumber,
                                            @RequestParam(required = false, defaultValue = "10") String pageSize)
    {
        ResponseData responseData = humanResourceService.findHumanResource(sort.toLowerCase(), orderBy.toUpperCase(), pageSize, pageNumber, selectValue, searchText);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
    
    // 소속/부서 조회
    @GetMapping("/dept")
    public ResponseEntity findHumanResourceDept(@RequestParam(defaultValue = "name") String sort,
                                                @RequestParam(defaultValue = "ASC") String orderBy,
                                                @RequestParam(required = false, defaultValue = "1") String pageNumber,
                                                @RequestParam(required = false, defaultValue = "10") String pageSize,
                                                @RequestParam(required = false) String name,
                                                @RequestParam(required = false) String affiliation,
                                                @RequestParam(required = false) String department)
    {
        ResponseData responseData = humanResourceService.findHumanResourceDept(sort.toLowerCase(), orderBy.toUpperCase(), pageSize, pageNumber, name, affiliation, department);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
}
