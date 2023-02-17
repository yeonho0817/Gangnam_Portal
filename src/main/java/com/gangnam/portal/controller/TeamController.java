package com.gangnam.portal.controller;

import com.gangnam.portal.domain.Department;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.TeamDTO;
import com.gangnam.portal.service.TeamService;
import io.lettuce.core.dynamic.annotation.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    // 소속 조회
    @GetMapping("/affiliation")
    @Operation(operationId = "getAffiliation", summary = "전체 소속 조회", description = "전체 소속을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 소속 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "전체 소속 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<List<TeamDTO.AffiliationNameDTO>> getAffiliation() {
        return teamService.getAffiliation();
    }
    
    // 부서 조회
    @GetMapping("/department")
    @Operation(operationId = "getDepartment", summary = "소속에 해당하는 부서 조회", description = "소속에 해당하는 부서를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "부서 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "부서 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<Department> getDepartment(@Param("affiliationId") Long affiliationId) {
        return teamService.getDepartment(affiliationId);
    }

    // 부서 목록 조회
    @GetMapping("/department/list")
    @Operation(operationId = "getDepartment", summary = "소속에 해당하는 부서 조회", description = "소속에 해당하는 부서를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "부서 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "부서 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<List<TeamDTO.DepartmentNameDTO>> getDepartmentList() {
        return teamService.getDepartmentList();
    }
    
    // 직급 조회
    @GetMapping("/ranks")
    @Operation(operationId = "getRank", summary = "직급 조회", description = "전체 직급을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 직급 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "전체 직급 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<List<TeamDTO.RankDTO>> getRanks() {
        return teamService.getRanks();
    }

}
