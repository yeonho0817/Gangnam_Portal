package com.gangnam.portal.controller;

import com.gangnam.portal.service.HumanResourceService;
import lombok.RequiredArgsConstructor;
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

    // 인력 조회
    @GetMapping("/management")
    public ResponseEntity findHumanResource(@RequestParam(defaultValue = "name") String sort,
                                            @RequestParam(defaultValue = "ASC") String orderBy,
                                            @RequestParam(required = false) String selectValue,
                                            @RequestParam(required = false) String searchTxt,
                                            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                            @RequestParam(required = false, defaultValue = "1") Integer pageNumber)
    {
        humanResourceService.findHumanResource(sort, orderBy, selectValue, searchTxt, pageSize, pageNumber);


        return null;
    }
    
    // 소속/부서 조회
    @GetMapping("/dept")
    public ResponseEntity findHumanResourceDept(@RequestParam(defaultValue = "name") String sort,
                                            @RequestParam(defaultValue = "ASC") String orderBy,
                                            @RequestParam(required = false) String name)
    {

        return null;
    }
}
