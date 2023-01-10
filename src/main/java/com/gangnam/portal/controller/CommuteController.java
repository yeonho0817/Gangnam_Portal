package com.gangnam.portal.controller;

import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.CommuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class CommuteController {
    private final CommuteService commuteService;

    // 출근 등록
    @PostMapping("/commute/start")
    public ResponseEntity commuteStart(@RequestBody @Valid CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        ResponseData responseData = commuteService.commuteStart(commuteRegisterDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 퇴근 등록
    @PostMapping("/commute/end")
    public ResponseEntity commuteEnd(@RequestBody CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {

        return null;
    }

    // 출퇴근 수정 - 관리자 기능
    @PutMapping("/commute/update")
    public ResponseEntity commuteUpdate(@RequestBody CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {

        return null;
    }
    
    // 월별 출퇴근 조회
    @GetMapping("")

    public ResponseEntity commuteMy(@RequestParam String year,
                                    @RequestParam String month) {

        return null;
    }

    // 월별 출퇴근 조회
    @GetMapping("/all")
    public ResponseEntity commuteAll(@RequestParam String year,
                                    @RequestParam String month) {


        return null;
    }
    
    // 출퇴근 현황 조회
    @GetMapping("/commute")
    public ResponseEntity commuteState(@RequestParam(defaultValue = "name") String sort,
                                       @RequestParam(defaultValue = "ASC") String orderBy,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate,
                                       @RequestParam(required = false) String name) {

        return null;
    }
}
