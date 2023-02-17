package com.gangnam.portal.service;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.QueryConditionDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.CommuteRepository;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.CommuteCustomRepository;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import com.gangnam.portal.util.holidayApi.HolidayApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommuteService {
    private final EmployeeRepository employeeRepository;
    private final CommuteRepository commuteRepository;
    private final CommuteCustomRepository commuteCustomRepository;
    private final EmployeeCustomRepository employeeCustomRepository;
    private final HolidayApi holidayApi;

    // 출근 등록
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteStart(UsernamePasswordAuthenticationToken authenticationToken) {
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

            if (today.equals(formatter.format(latestCommute.get().getRegisterDate())))
                if (! StringUtils.hasText(latestCommute.get().getEndDate().toString())) throw new CustomException(ErrorStatus.COMMUTE_ALREADY_START);
                else throw new CustomException(ErrorStatus.COMMUTE_ALREADY_EXISTS);
        }

        Employee findEmployee = employeeRepository.findById(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));
        
        Commute newCommute = Commute.builder()
                .employee(findEmployee)
                .startDate(new Date())
                .registerDate(new Date())
                .build();

        commuteRepository.save(newCommute);

        return new ResponseData(Status.COMMUTE_START_SUCCESS, Status.COMMUTE_START_SUCCESS.getDescription());
    }

    // 퇴근 등록  :  무조건 전날껄 등록해야 함 -> 출근이든 퇴근이든
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteEnd(UsernamePasswordAuthenticationToken authenticationToken) {
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
            throw new CustomException(ErrorStatus.COMMUTE_END_FORBIDDEN);

        findLatestCommute.updateEndDate(new Date());

        return new ResponseData(Status.COMMUTE_END_SUCCESS, Status.COMMUTE_END_SUCCESS.getDescription());
    }

    // 출퇴근 수정 - 관리자
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteUpdateAdmin(CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        if (commuteUpdateDTO.getStartDate().after(commuteUpdateDTO.getEndDate())) throw new CustomException(ErrorStatus.COMMUTE_END_DATE_ERROR);
        if (commuteUpdateDTO.getStartDate().after(new Date())) throw new CustomException(ErrorStatus.COMMUTE_DATE_ERROR);
        if (commuteUpdateDTO.getEndDate().after(new Date())) throw new CustomException(ErrorStatus.COMMUTE_DATE_ERROR);

        Commute findCommuteInfo =  commuteRepository.findById(commuteUpdateDTO.getCommuteId())
                .orElseThrow(() -> new CustomException(ErrorStatus.COMMUTE_READ_FAILED));

        findCommuteInfo.updateStartDate(commuteUpdateDTO.getStartDate());
        findCommuteInfo.updateEndDate(commuteUpdateDTO.getEndDate());

        return new ResponseData(Status.COMMUTE_UPDATE_SUCCESS, Status.COMMUTE_UPDATE_SUCCESS.getDescription());
    }

    // 출퇴근 등록 - 관리자
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData commuteCreateAdmin(CommuteDTO.CommuteRegisterDTO commuteRegisterDTO) {
        if (commuteRegisterDTO.getStartDate().after(new Date())) throw new CustomException(ErrorStatus.COMMUTE_DATE_ERROR);
        if (commuteRegisterDTO.getEndDate().after(new Date())) throw new CustomException(ErrorStatus.COMMUTE_DATE_ERROR);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());

        // 등록일이 오늘 넘어가면, 퇴근이 출근보다 빠르면 error
        if (commuteRegisterDTO.getRegisterDate().compareTo(new Date()) > 0) throw new CustomException(ErrorStatus.COMMUTE_REGISTER_DATE_ERROR);
        if (commuteRegisterDTO.getStartDate().after(commuteRegisterDTO.getEndDate())) throw new CustomException(ErrorStatus.COMMUTE_END_DATE_ERROR);

        Employee findEmployee = employeeRepository.findById(commuteRegisterDTO.getEmployeeId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        // 등록일 + employee id 로 이미 있으면 안됨.
        Optional<Commute> findCommute = commuteCustomRepository.findCommute(commuteRegisterDTO.getEmployeeId(), commuteRegisterDTO.getRegisterDate());
        if (findCommute.isPresent()) throw new CustomException(ErrorStatus.COMMUTE_ALREADY_EXISTS);

        Commute newCommute = Commute.builder()
                .employee(findEmployee)
                .registerDate(commuteRegisterDTO.getRegisterDate())
                .startDate(commuteRegisterDTO.getStartDate())
                .endDate(commuteRegisterDTO.getEndDate())

                .build();

        commuteRepository.save(newCommute);

        return new ResponseData(Status.COMMUTE_CREATE_SUCCESS, Status.COMMUTE_CREATE_SUCCESS.getDescription());
    }
    // 월별 출퇴근 조회
    @Transactional(readOnly = true)
    public ResponseData<List<CommuteDTO.CommuteListBoard>> commuteBoard(UsernamePasswordAuthenticationToken authenticationToken, CommuteDTO.CommuteBoardData commuteBoardData) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        if (StringUtils.hasText(commuteBoardData.getRange()) && commuteBoardData.getRange().equals("my") ) {
            return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteCustomRepository.readCommute(authenticationDTO.getId(), commuteBoardData.getYear(), commuteBoardData.getMonth()));
        } else {
//            return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteCustomRepository.readCommute(null, commuteBoardData.getYear(), commuteBoardData.getMonth()).stream().sorted(Comparator.comparing(CommuteDTO.CommuteListBoard::getStartDate)).collect(Collectors.toList()));
            return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteCustomRepository.readCommute(null, commuteBoardData.getYear(), commuteBoardData.getMonth()));
        }
    }

    // 출퇴근 현황 조회
    @Transactional(readOnly = true)
    public ResponseData<CommuteDTO.CommuteState> commuteStateList(String sort, String orderBy, String pageNumber, String pageSize, String startDate, String endDate, Long employeeId) {
        try {
            QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize, startDate, endDate);

            Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                    Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));

            if (employeeId == 0) employeeId = null;


            // 전체 페이지 갯수 표시 해줘야 함
            Page<CommuteDTO.CommuteStateData> commuteStateList = commuteCustomRepository.readCommuteState(pageable, queryConditionDTO.getStartDate(), queryConditionDTO.getEndDate(), employeeId);


            List<CommuteDTO.CommuteStateData> commuteStateDataList = getStateData(commuteStateList.toList());


            return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new CommuteDTO.CommuteState(commuteStateList.getTotalPages(), (int) commuteStateList.getTotalElements(), commuteStateDataList));
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.HOLIDAY_INFO_FAIL);
        }
    }

    // excel
    @Transactional(readOnly = true)
    public ResponseData<List<CommuteDTO.CommuteExcelData>> commuteExcel(String startDate, String endDate, Long employeeId) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(startDate, endDate);
        if (employeeId == 0) employeeId = null;
        List<CommuteDTO.CommuteExcelData> commuteExcelDataList = commuteCustomRepository.findCommuteExcel(queryConditionDTO.getStartDate(), queryConditionDTO.getEndDate(), employeeId);
        List<EmployeeDTO.EmployeeSimpleInfo> employeeList = employeeCustomRepository.findByEmployeeIdOrderByIdAsc(employeeId);

        // 시작 날짜
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(queryConditionDTO.getStartDate());

        // 끝 날짜
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(queryConditionDTO.getEndDate());

        // excel 데이터 추출
        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), getExcelData(commuteExcelDataList, employeeList, startCalendar, endCalendar));
    }

    // 휴일 + 요일 가공
    private List<CommuteDTO.CommuteStateData> getStateData(List<CommuteDTO.CommuteStateData> commuteStateDataList) throws Exception {
        Calendar calendar = Calendar.getInstance();
        return commuteStateDataList.stream()
                .peek(commuteStateData -> {
                    try {
                        calendar.clear();
                        calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(commuteStateData.getRegisterDate()));

                        // 요일 설정
                        commuteStateData.setRegisterDate(commuteStateData.getRegisterDate() + " (" + getDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)).charAt(0) + ")");

                        // 휴일 설정
                        if (commuteStateData.getHolidayName() == null ) {
                            if (getDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)).equals("일요일")) commuteStateData.setHolidayName("일요일");
                            else if (getDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)).equals("토요일")) commuteStateData.setHolidayName("토요일");
                        }

                    } catch (ParseException e) {
                        throw new CustomException(ErrorStatus.HOLIDAY_INFO_FAIL);
                    }
                })
                .collect(Collectors.toList());
    }

    // excel 가공
    private List<CommuteDTO.CommuteExcelData> getExcelData(List<CommuteDTO.CommuteExcelData> commuteExcelDataList, List<EmployeeDTO.EmployeeSimpleInfo> employeeList, Calendar startCalendar, Calendar endCalendar) {
        List<CommuteDTO.CommuteExcelData> newCommuteExcelDataList  = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        int index = 0;

        for (int i = 0; i<employeeList.size(); i++) {
            // 날짜 초기화
            calendar.clear();
            calendar.setTimeInMillis(startCalendar.getTimeInMillis());

            for (; calendar.compareTo(endCalendar) < 1; calendar.add(Calendar.DATE, 1)) {
                int numDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

                if (index < commuteExcelDataList.size() &&
                    Objects.equals(employeeList.get(i).getEmployeeNo(), commuteExcelDataList.get(index).getEmployeeNo()) &&
                    formatter.format(calendar.getTime()).equals(commuteExcelDataList.get(index).getRegisterDate()) ) {
                        newCommuteExcelDataList.add(commuteExcelDataBuilder(employeeList.get(i), numDayOfTheWeek, commuteExcelDataList.get(index).getRegisterDate(), commuteExcelDataList.get(index).getStartDate(), commuteExcelDataList.get(index).getEndDate()));
                        index++;
                } else {
                    if (numDayOfTheWeek == 1 || numDayOfTheWeek == 7) {    // 주말 : "-"
                        newCommuteExcelDataList.add(commuteExcelDataBuilder(employeeList.get(i),  numDayOfTheWeek, formatter.format(calendar.getTime()), "-", "-"));
                    } else {    // 평일 : "0"
                        newCommuteExcelDataList.add(commuteExcelDataBuilder(employeeList.get(i), numDayOfTheWeek, formatter.format(calendar.getTime()), "0", "0"));
                    }
                }
            }
        }

        return newCommuteExcelDataList;
    }

    private CommuteDTO.CommuteExcelData commuteExcelDataBuilder(EmployeeDTO.EmployeeSimpleInfo employeeInfo, int numDayOfTheWeek, String registerDate, String startDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return CommuteDTO.CommuteExcelData.builder()
                    .employeeNo(employeeInfo.getEmployeeNo())
                    .nameKr(employeeInfo.getNameKr())
                    .registerDate(registerDate)
                    .dayOfTheWeek(getDayOfTheWeek(numDayOfTheWeek))
                    .startDate(startDate)
                    .endDate(endDate)

                    .totalCommuteTime(
                            startDate == null || endDate == null ?
                                    0.0d :
                                    startDate.equals("0") || endDate.equals("0") || startDate.equals("-") || endDate.equals("-") ?
                                             0.0d :
                                            Math.round(
                                                    ((float)(formatter.parse(endDate).getTime() - formatter.parse(startDate).getTime()) / 1000 / 60 / 60) * 10 ) / 10.0
                    )

                    .build();
        } catch (ParseException ignored) { }
        return null;

    }

    // 요일 반환
    private String getDayOfTheWeek(int numDayOfTheWeek) {
        String[] week = {"일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"};

        return week[--numDayOfTheWeek];
    }

}
