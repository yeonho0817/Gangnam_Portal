package com.gangnam.portal.controller;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.TeamDTO;
import com.gangnam.portal.service.HumanResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hr")
public class HumanResourceController {
    private final HumanResourceService humanResourceService;

    // 소속/부서 이름 조회      -  O
    @GetMapping("/teamList")
    @Operation(operationId = "teamList", summary = "소속/부서 이름 조회", description = "모든 소속/부서의 이름을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소속/부서 이름 조회",
                    content = {@Content(mediaType = "application/json")}),

    })
    public ResponseData<List<TeamDTO.AffiliationNameDTO>> findAffiliationDepartment() {
        return humanResourceService.findAffiliationDepartment();
    }


    // 인력 조회        O
    @GetMapping("/management")
    @Operation(operationId = "hrManagement", summary = "전체 인력 조회", description = "전체 사원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 인력 정보 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "전체 인력 정보 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EmployeeDTO.HRInfo> findHumanResource(
                                                            @Parameter(description = "정렬 기준") @RequestParam(defaultValue = "rank") String sort,
                                                            @Parameter(description = "정렬 방식(ASC, DESC)") @RequestParam(defaultValue = "ASC") String orderBy,
                                                            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "1") String pageNumber,
                                                            @Parameter(description = "페이지 양") @RequestParam(defaultValue = "10") String pageSize ,
                                                            @Parameter(description = "검색 기준") @RequestParam(required = false) String selectValue,
                                                            @Parameter(description = "검색할 데이터") @RequestParam(required = false) String searchText) {
        return humanResourceService.findHumanResource(sort.toLowerCase(), orderBy.toUpperCase(), pageSize, pageNumber, selectValue, searchText);
    }

    // 소속/부서 조회     O
    @GetMapping("/dept")
    @Operation(operationId = "hrDept", summary = "전체 사원 조회", description = "소속/부서 기준으로 전체 사원을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소속/부서 기준 전체 사원 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "소속/부서 기준 전체 사원 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<EmployeeDTO.HRDepartmentInfo> findHumanResourceDept(
                                                            @RequestParam(defaultValue = "name") String sort,
                                                            @RequestParam(defaultValue = "ASC") String orderBy,
                                                            @RequestParam(required = false, defaultValue = "1") String pageNumber,
                                                            @RequestParam(required = false, defaultValue = "10") String pageSize,
                                                            @RequestParam(required = false) String name,
                                                            @RequestParam(required = false) String affiliation,
                                                            @RequestParam(required = false) String department) {
        return humanResourceService.findHumanResourceDept(sort.toLowerCase(), orderBy.toUpperCase(), pageSize, pageNumber, name, affiliation, department);
    }
}
