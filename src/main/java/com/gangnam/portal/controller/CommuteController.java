package com.gangnam.portal.controller;

import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.CommuteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class CommuteController {
    private final CommuteService commuteService;

    // 출근 등록
    @PostMapping("/commute/start")
    public ResponseEntity commuteStart(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken/*,
                                        @RequestBody @Valid CommuteDTO.CommuteRegisterDTO commuteRegisterDTO*/) {
        ResponseData responseData = commuteService.commuteStart(usernamePasswordAuthenticationToken/*, commuteRegisterDTO*/);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 퇴근 등록
    @PostMapping("/commute/end")
    public ResponseEntity commuteEnd(UsernamePasswordAuthenticationToken authenticationToken/*,
                                     @RequestBody CommuteDTO.CommuteRegisterDTO commuteRegisterDTO*/) {
        ResponseData responseData = commuteService.commuteEnd(authenticationToken/*, commuteRegisterDTO*/);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 출퇴근 수정 - 관리자 기능
    @PutMapping("/commute")
    public ResponseEntity commuteUpdate(@RequestBody @Valid CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        ResponseData responseData = commuteService.commuteUpdate(commuteUpdateDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 출퇴근 수정 - 관리자 기능
    @PostMapping("/commute")
    public ResponseEntity commuteCreate(@RequestBody @Valid CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        ResponseData responseData = commuteService.commuteUpdate(commuteUpdateDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
    
    // 월별 출퇴근 조회
    @GetMapping("/")
    public ResponseEntity commuteMy(@ModelAttribute @Valid CommuteDTO.CommuteDateInfo commuteDateInfo,
                                    UsernamePasswordAuthenticationToken authenticationToken) {
        ResponseData responseData = commuteService.commuteMy(authenticationToken, commuteDateInfo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }

    // 월별 출퇴근 조회
    @GetMapping("/all")
    public ResponseEntity commuteAll(@ModelAttribute @Valid CommuteDTO.CommuteDateInfo commuteDateInfo) {
        ResponseData responseData = commuteService.commuteAll(commuteDateInfo);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
    
    // 출퇴근 현황 조회
    @GetMapping("/commute")
    public ResponseEntity commuteStateList(@RequestParam(defaultValue = "name") String sort,
                                           @RequestParam(defaultValue = "ASC") String orderBy,
                                           @RequestParam(defaultValue = "1") String pageNumber,
                                           @RequestParam(defaultValue = "10") String pageSize ,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate,
                                           @RequestParam(required = false) String name ) {

        ResponseData responseData = commuteService.commuteStateList(sort.toLowerCase(), orderBy.toUpperCase(), pageNumber, pageSize, startDate, endDate, name);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseData);
    }
}
