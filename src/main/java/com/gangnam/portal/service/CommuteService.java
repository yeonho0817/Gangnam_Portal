package com.gangnam.portal.service;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.QueryConditionDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.CommuteRepository;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.CommuteCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommuteService {
    private final EmployeeRepository employeeRepository;
    private final CommuteRepository commuteRepository;
    private final CommuteCustomRepository commuteCustomRepository;

    // 출근 등록

    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteStart(UsernamePasswordAuthenticationToken authenticationToken, CommuteDTO.CommuteStartEndDTO commuteStartEndDTO) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Optional<Commute> latestCommute = commuteCustomRepository.findLatestCommute(authenticationDTO.getId());

        /*
            최근께 없으면 바로 등록
            최근께 있으면
                - endDate == null : 출근을 이미 찍고 퇴근만 안찍음, 등록 불가능
                - endDate != null : 출근을 안찍음, 등록
        */

        if (latestCommute.isPresent()) {
            if (latestCommute.get().getEndDate() == null) throw new CustomException(ErrorStatus.COMMUTE_START_FORBIDDEN);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String today = formatter.format(new Date());

            if (today.equals(formatter.format(latestCommute.get().getRegisterDate()))) throw new CustomException(ErrorStatus.COMMUTE_ALREADY_START);
        }

        Employee findEmployee = employeeRepository.findById(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));
        
        Commute newCommute = Commute.builder()
                .employee(findEmployee)
                .startDate(commuteStartEndDTO.getDate())
                .registerDate(new Date())
                .build();

        commuteRepository.save(newCommute);

        return new ResponseData(Status.COMMUTE_START_SUCCESS, Status.COMMUTE_START_SUCCESS.getDescription());
    }

    // 퇴근 등록  :  무조건 전날껄 등록해야 함 -> 출근이든 퇴근이든
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteEnd(UsernamePasswordAuthenticationToken authenticationToken, CommuteDTO.CommuteStartEndDTO commuteStartEndDTO) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        /*
            최근께 없으면 바로 등록 불가능
            최근께 있으면
                - endDate == null : 출근 찍고 퇴근 안찍음, 등록 가능
                - endDate != null
                    - 오늘꺼면 : 새로운 시간으로 덮어 씌움
                    - 그게 아니면,  등록 불가능
        */

        Commute findLatestCommute = commuteCustomRepository.findLatestCommute(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.COMMUTE_END_FORBIDDEN));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());

        if (findLatestCommute.getEndDate() != null && ! formatter.format(findLatestCommute.getRegisterDate()).equals(today))
            throw new CustomException(ErrorStatus.COMMUTE_ALREADY_END);

        findLatestCommute.updateEndDate(commuteStartEndDTO.getDate());

        return new ResponseData(Status.COMMUTE_END_SUCCESS, Status.COMMUTE_END_SUCCESS.getDescription());
    }

    // 출퇴근 수정 - 관리자
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteUpdateAdmin(CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        // 등록일 + employee id 로 이미 있는 건지 조사
        Commute findCommuteInfo =  commuteRepository.findById(commuteUpdateDTO.getCommuteId())
                .orElseThrow(() -> new CustomException(ErrorStatus.COMMUTE_READ_FAILED));

        findCommuteInfo.updateStartDate(commuteUpdateDTO.getStartDate());
        findCommuteInfo.updateEndDate(commuteUpdateDTO.getEndDate());

        return new ResponseData(Status.COMMUTE_UPDATE_SUCCESS, Status.COMMUTE_UPDATE_SUCCESS.getDescription());
    }

    // 출퇴근 등록 - 관리자
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteCreateAdmin(CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        // 등록일 + employee id 로 이미 있는 건지 조사
        Employee findEmployee = employeeRepository.findById(commuteRegisterDTO.getEmployeeId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        Commute newCommute = Commute.builder()
                .employee(findEmployee)
                .registerDate(commuteRegisterDTO.getRegisterDate())
                .startDate(commuteRegisterDTO.getStartDate())
                .endDate(commuteRegisterDTO.getEndDate())

                .build();

        commuteRepository.save(newCommute);

        return new ResponseData(Status.COMMUTE_UPDATE_SUCCESS, Status.COMMUTE_UPDATE_SUCCESS.getDescription());
    }

    // 월별 출퇴근 조회 - 본인
    @Transactional(readOnly = true)
    public ResponseData<List<CommuteDTO.CommuteListBoard>> commuteMy(UsernamePasswordAuthenticationToken authenticationToken, CommuteDTO.CommuteDateInfo commuteDateInfo) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        List<CommuteDTO.CommuteListBoard> commuteListBoardList = commuteCustomRepository.readCommute(authenticationDTO.getId(),
                Integer.valueOf(commuteDateInfo.getYear()),
                Integer.valueOf((commuteDateInfo.getMonth()))
        );

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteListBoardList);
    }

    // 월별 출퇴근 조회 - 전체
    @Transactional(readOnly = true)
    public ResponseData<List<CommuteDTO.CommuteListBoard>> commuteAll(CommuteDTO.CommuteDateInfo commuteDateInfo) {
        List<CommuteDTO.CommuteListBoard> commuteListBoardList = commuteCustomRepository.readCommute(null,
                Integer.valueOf(commuteDateInfo.getYear()),
                Integer.valueOf((commuteDateInfo.getMonth()))
        );

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteListBoardList);
    }


    // 출퇴근 현황 조회
    @Transactional(readOnly = true)
    public ResponseData<CommuteDTO.CommuteState> commuteStateList(String sort, String orderBy, String pageNumber, String pageSize, String startDate, String endDate, String name) {

        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize, startDate, endDate);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));
        
        // 전체 페이지 갯수 표시 해줘야 함
        Page<CommuteDTO.CommuteStateData> commuteStateList = commuteCustomRepository.readCommuteState(pageable, queryConditionDTO.getStartDate(), queryConditionDTO.getEndDate(), name);

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new CommuteDTO.CommuteState(commuteStateList.getTotalPages(), commuteStateList.stream().collect(Collectors.toList())));
    }
}
