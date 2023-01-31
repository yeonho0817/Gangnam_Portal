package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    // 사원 조회        O
    @GetMapping("/hr/info")
    @Operation(operationId = "hrInfo", summary = "자신의 정보 조회", description = "자신의 사원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "사원 정보 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(@ApiIgnore UsernamePasswordAuthenticationToken authentication) {
        return employeeService.findEmployeeInfo(authentication);
    }
    
    // 회원 수정            O
    @PutMapping("/hr/info")
    @Operation(operationId = "hrInfoUpdate", summary = "자신의 정보 수정", description = "자신의 사원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 정보 수정",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "사원 정보 수정 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData updateEmployeeInfo(@ApiIgnore UsernamePasswordAuthenticationToken authenticationToken,
                                           @Parameter(description = "사원 정보") @Valid @RequestBody EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        return employeeService.updateEmployeeInfo(authenticationToken, updateInfoDTO);
    }

    // 출퇴근 수정 시 직원 목록       O
    @GetMapping("/hr/names")
    @Operation(operationId = "hrNameList", summary = "전체 사원 이름 조회", description = "모든 사원의 이름을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 사원 이름 조회",
                    content = {@Content(mediaType = "application/json")}),
    })
    public ResponseData<List<EmployeeDTO.EmployeeNameList>> readEmployeeNameList() {
        return employeeService.readEmployeeNameList();

    }
}
