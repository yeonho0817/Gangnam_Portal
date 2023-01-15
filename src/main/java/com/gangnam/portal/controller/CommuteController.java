package com.gangnam.portal.controller;

import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.service.CommuteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class CommuteController {
    private final CommuteService commuteService;

    // 출근 등록
    @PostMapping("/commute/start")
    @Operation(operationId = "commuteStart", summary = "출근 등록 API", description = "출근을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출근 등록",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData commuteStart(UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken,
                                        @RequestBody @Valid CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        ResponseData responseData = commuteService.commuteStart(usernamePasswordAuthenticationToken, commuteRegisterDTO);

        return responseData;
    }

    // 퇴근 등록
    @PostMapping("/commute/end")
    @Operation(operationId = "commuteEnd", summary = "퇴근 등록 API", description = "퇴근을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "퇴근 등록",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData commuteEnd(UsernamePasswordAuthenticationToken authenticationToken,
                                     @RequestBody CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        ResponseData responseData = commuteService.commuteEnd(authenticationToken, commuteRegisterDTO);

        return responseData;
    }

    // 출퇴근 수정 - 관리자 기능
    @PutMapping("/commute")
    @Operation(operationId = "commuteUpdate", summary = "출근 수정 API(관리자 권한)", description = "사원의 출퇴근 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 수정 - 관리자",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData commuteUpdate(@RequestBody @Valid CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        ResponseData responseData = commuteService.commuteUpdate(commuteUpdateDTO);

        return responseData;
    }

    // 출퇴근 수정 - 관리자 기능
    @PostMapping("/commute")
    @Operation(operationId = "commuteCreate", summary = "출근 등록 API(관리자 권한)", description = "사원이 등록하지 못한 출퇴근 정보를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 등록 - 관리자",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData commuteCreate(@RequestBody @Valid CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        ResponseData responseData = commuteService.commuteUpdate(commuteUpdateDTO);

        return responseData;
    }
    
    // 월별 출퇴근 조회
    @GetMapping("/")
    @Operation(operationId = "myCommute", summary = "월별 출퇴근 조회 - 본인", description = "자신의 월별 출퇴근 현황을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월별 출퇴근 - 본인",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData<List<CommuteDTO.CommuteListBoard>> commuteMy(@ModelAttribute @Valid CommuteDTO.CommuteDateInfo commuteDateInfo,
                                                                     UsernamePasswordAuthenticationToken authenticationToken) {
        ResponseData responseData = commuteService.commuteMy(authenticationToken, commuteDateInfo);

        return responseData;
    }

    // 월별 출퇴근 조회
    @GetMapping("/all")
    @Operation(operationId = "allCommute", summary = "월별 출퇴근 조회 - 전체", description = "모든 사원의 월별 출퇴근 현황을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월별 출퇴근 - 전체",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData<List<CommuteDTO.CommuteListBoard>> commuteAll(@ModelAttribute @Valid CommuteDTO.CommuteDateInfo commuteDateInfo) {
        ResponseData responseData = commuteService.commuteAll(commuteDateInfo);

        return responseData;
    }
    
    // 출퇴근 현황 조회
    @GetMapping("/commute")
    @Operation(operationId = "commuteState", summary = "출퇴근 현황 조회", description = "사원의 기록된 출퇴근 현황을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출퇴근 현황 조회",
                    content = {@Content(mediaType = "application/json"/*, schema = @Schema(implementation = AuthDTO.TokenDTO.class)*/)}),
//        @ApiResponse(responseCode = "4XX, 5XX", description = "버스 등록 실패",
//                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorVO.class)))
    })
    public ResponseData<CommuteDTO.CommuteState> commuteStateList(@RequestParam(defaultValue = "name") String sort,
                                           @RequestParam(defaultValue = "ASC") String orderBy,
                                           @RequestParam(defaultValue = "1") String pageNumber,
                                           @RequestParam(defaultValue = "10") String pageSize ,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate,
                                           @RequestParam(required = false) String name ) {

        ResponseData responseData = commuteService.commuteStateList(sort.toLowerCase(), orderBy.toUpperCase(), pageNumber, pageSize, startDate, endDate, name);

        return responseData;
    }
}
