package com.gangnam.portal.controller;

import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.ErrorResponse;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.CommuteService;
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
@RequiredArgsConstructor
@RequestMapping("/commute")
public class CommuteController {
    private final CommuteService commuteService;

    // 출근 등록    O
    @PostMapping("/start")
    @Operation(operationId = "commuteStart", summary = "출근 등록 API", description = "출근을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출근 등록",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "출근 등록 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData commuteStart(@ApiIgnore @Parameter(hidden = true) UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                                     @Parameter(description = "출근 시간 정보") @RequestBody @Valid CommuteDTO.CommuteStartEndDTO commuteStartEndDTO) {
        return commuteService.commuteStart(usernamePasswordAuthenticationToken, commuteStartEndDTO);
    }

    // 퇴근 등록        O
    @PostMapping("/end")
    @Operation(operationId = "commuteEnd", summary = "퇴근 등록 API", description = "퇴근을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퇴근 등록",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "퇴근 등록 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData commuteEnd(@ApiIgnore UsernamePasswordAuthenticationToken authenticationToken,
                                   @Parameter(description = "퇴근 시간 정보") @RequestBody CommuteDTO.CommuteStartEndDTO commuteStartEndDTO) {
        return commuteService.commuteEnd(authenticationToken, commuteStartEndDTO);
    }

    // 출퇴근 수정 - 관리자 기능      O
    @PutMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(operationId = "commuteUpdate", summary = "출근 수정 API(관리자 권한)", description = "사원의 출퇴근 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 수정",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "출퇴근 수정 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData commuteUpdate(@Parameter(description = "출퇴근 수정 정보") @RequestBody @Valid CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        return commuteService.commuteUpdateAdmin(commuteUpdateDTO);
    }

    // 출퇴근 등록 - 관리자 기능      O

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(operationId = "commuteCreate", summary = "출근 등록 API(관리자 권한)", description = "사원이 등록하지 못한 출퇴근 정보를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 등록",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class))}),
            @ApiResponse(responseCode = "4XX", description = "출퇴근 등록 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData commuteCreate(@Parameter(description = "출퇴근 등록 정보") @RequestBody @Valid CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        return commuteService.commuteCreateAdmin(commuteRegisterDTO);
    }

    // 월별 출퇴근 조회        O
    @GetMapping("/board")
    @Operation(operationId = "commuteBoard", summary = "월별 출퇴근 조회", description = "월별 출퇴근 현황을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월별 출퇴근",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "월별 출퇴근 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<List<CommuteDTO.CommuteListBoard>> commuteMy(@Parameter(description = "월별 출퇴근 정보") @ModelAttribute @Valid CommuteDTO.CommuteBoardData commuteBoardData,
                                                                     @ApiIgnore @Parameter(hidden = true) UsernamePasswordAuthenticationToken authenticationToken) {
        return commuteService.commuteBoard(authenticationToken, commuteBoardData);
    }

    // 출퇴근 현황 조회    O
    @GetMapping("")
    @Operation(operationId = "commuteState", summary = "출퇴근 현황 조회", description = "사원의 기록된 출퇴근 현황을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 현황 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "출퇴근 현황 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<CommuteDTO.CommuteState> commuteStateList(@Parameter(description = "정렬 기준") @RequestParam(defaultValue = "date") String sort,
                                                                  @Parameter(description = "정렬 방식(ASC, DESC)") @RequestParam(defaultValue = "DESC") String orderBy,
                                                                  @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "1") String pageNumber,
                                                                  @Parameter(description = "페이지 양") @RequestParam(defaultValue = "10") String pageSize,
                                                                  @Parameter(description = "시작 날짜") @RequestParam(required = false) String startDate,
                                                                  @Parameter(description = "끝 날짜") @RequestParam(required = false) String endDate,
                                                                  @Parameter(description = "사원 ID(전체는 0)") @RequestParam(defaultValue = "0", required = false) Long employeeId) {
        return commuteService.commuteStateList(sort.toLowerCase(), orderBy.toUpperCase(), pageNumber, pageSize, startDate, endDate, employeeId);
    }

    // 출퇴근 엑셀 API
    @GetMapping("/excel")
    @Operation(operationId = "commuteState(excel)", summary = "출퇴근 현황 조회 - 엑셀 추출", description = "엑셀로 출력하기 위한 출퇴근 현황 데이터를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 현황 Excel 데이터 조회",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "4XX", description = "출퇴근 현황 Excel 데이터 조회 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseData<List<CommuteDTO.CommuteExcelData>> excelData(@Parameter(description = "시작 날짜") @RequestParam(required = false) String startDate,
                                                                     @Parameter(description = "끝 날짜") @RequestParam(required = false) String endDate,
                                                                     @Parameter(description = "이름") @RequestParam(required = false) Long employeeId) {

        return commuteService.commuteExcel(startDate, endDate, employeeId);
    }
}
