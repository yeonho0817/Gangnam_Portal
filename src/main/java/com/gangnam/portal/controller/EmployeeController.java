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
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 사원 추가
    @PostMapping("/hr/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(operationId = "hrCreateAdmin", summary = "사원 추가(관리자)", description = "사원의 정보를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 정보 추가",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "사원 정보 추가 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData saveEmployee(@Valid @RequestBody EmployeeDTO.EmployeeAdminInfo employeeSaveInfo) {
        System.out.println(employeeSaveInfo.toString());
        return employeeService.saveEmployee(employeeSaveInfo);
    }

    // 사원 수정
    @PutMapping("/hr/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(operationId = "hrUpdateAdmin", summary = "사원 수정(관리자)", description = "사원의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 정보 수정",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "사원 정보 수정 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData updateEmployeeInfo(@Valid @RequestBody EmployeeDTO.EmployeeAdminInfo employeeUpdateInfo) {
        return employeeService.updateEmployeeInfo(employeeUpdateInfo);
    }

    // 사원 정보 조회
    @GetMapping("/hr/info")
    @Operation(operationId = "hrEmployeeInfo", summary = "사원 정보 조회", description = "사원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사원 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "사원 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EmployeeDTO.EmployeeInfoDTO> findMyInfo(@RequestParam(name = "employeeId") Long employeeId) {
        return employeeService.findEmployeeInfo(employeeId);
    }


    // 내 정보 조회        O
    @GetMapping("/hr/my")
    @Operation(operationId = "hrMyInfo", summary = "자신의 정보 조회", description = "자신의 사원 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "내 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EmployeeDTO.EmployeeInfoDTO> findMyInfo(@ApiIgnore UsernamePasswordAuthenticationToken authentication) {
        return employeeService.findMyInfo(authentication);
    }
    
    // 내 정보 수정            O
    @PutMapping("/hr/my")
    @Operation(operationId = "hrMyInfoUpdate", summary = "자신의 정보 수정", description = "자신의 사원 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 수정",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "내 정보 수정 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData updateMyInfo(@ApiIgnore UsernamePasswordAuthenticationToken authenticationToken,
                                       @Parameter(description = "사원 정보") @Valid @ModelAttribute EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        return employeeService.updateMyInfo(authenticationToken, updateInfoDTO);
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
