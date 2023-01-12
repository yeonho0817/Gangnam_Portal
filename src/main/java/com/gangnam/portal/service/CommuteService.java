package com.gangnam.portal.service;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.dto.CommuteDTO;
import com.gangnam.portal.dto.Response.AuthenticationDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.CommuteRepository;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.CommuteCustomRepository;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommuteService {
    private final EmployeeCustomRepository employeeCustomRepository;
    private final EmployeeRepository employeeRepository;
    private final CommuteRepository commuteRepository;
    private final CommuteCustomRepository commuteCustomRepository;

    // 출근 등록

    public ResponseData commuteStart(UsernamePasswordAuthenticationToken authenticationToken/*, CommuteDTO.CommuteRegisterDTO commuteRegisterDTO*/) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Optional<Employee> findEmployee = employeeRepository.findById(authenticationDTO.getId());
        if (findEmployee.isEmpty()) return new ResponseData(Status.NOT_FOUND_EMPLOYEE, Status.NOT_FOUND_EMPLOYEE.getDescription());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());
        
        // 가장 최근 것
        Optional<Commute> findLatestCommute = commuteCustomRepository.findLatestCommute(authenticationDTO.getId());

        // 등록되어 있는게 없을 때는 출근 가능
        // 가장 최근에 가져 온것이 오늘이 아닐 때 -> 퇴근이 비어있으면 불가능
        if (formatter.format(findLatestCommute.get().getRegisterDate()) != formatter.format(new Date())) {
            if (findLatestCommute.isPresent() && findLatestCommute.get().getEndDate() == null)
                return new ResponseData(Status.COMMUTE_END_FORBIDDEN, Status.COMMUTE_END_FORBIDDEN.getDescription());
        } else {
            // 가장 최근에 가져온 것이 오늘일 때 == 이미 출근을 찍음
            return new ResponseData(Status.COMMUTE_ALREADY_START, Status.COMMUTE_ALREADY_START.getDescription());
        }
//        Optional<Commute> findTodayCommute = commuteCustomRepository.isAlreadyStart(today, authenticationDTO.getId());
//        if (findTodayCommute.isPresent()) return new ResponseData(Status.COMMUTE_START_FAILED, Status.COMMUTE_START_FAILED.getDescription());
        
        Commute newCommute = Commute.builder()
                .employee(findEmployee.get())
                .registerDate(new Date())
                .startDate(new Date())
                .build();

        commuteRepository.save(newCommute);

        return new ResponseData(Status.COMMUTE_START_SUCCESS, Status.COMMUTE_START_SUCCESS.getDescription());
    }

    // 퇴근 등록
    // 무조건 전날껄 등록해야 함 -> 출근이든 퇴근이든
    public ResponseData commuteEnd(UsernamePasswordAuthenticationToken authenticationToken/*, CommuteDTO.CommuteRegisterDTO commuteRegisterDTO*/) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Optional<Employee> findEmployee = employeeRepository.findById(authenticationDTO.getId());
        if (findEmployee.isEmpty()) return new ResponseData(Status.NOT_FOUND_EMAIL, Status.NOT_FOUND_EMAIL.getDescription());

        // 가장 최근 것
        Optional<Commute> findLatestCommute = commuteCustomRepository.findLatestCommute(authenticationDTO.getId());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(new Date());

        // 아직 출근하지 않은 경우 + 없는 경우
        if (findLatestCommute.isEmpty() || formatter.format(findLatestCommute.get().getRegisterDate()) != formatter.format(new Date())) {
            return new ResponseData(Status.COMMUTE_START_FORBIDDEN, Status.COMMUTE_START_FORBIDDEN.getDescription());
        } else {  // 이미 퇴근한 경우
            if (findLatestCommute.get().getEndDate() != null)
                return new ResponseData(Status.COMMUTE_ALREADY_END, Status.COMMUTE_ALREADY_END.getDescription());
        }

        findLatestCommute.get().updateEndDate(new Date());
//        commuteRepository.updateCommuteEnd(findLatestCommute.get().getId(), new Date());

        return new ResponseData(Status.COMMUTE_END_SUCCESS, Status.COMMUTE_END_SUCCESS.getDescription());
    }

    // 출퇴근 수정  => 날짜 + 회원 ID로 비교
    // 기존께 없는데 등록할 때
    // 기존께 있는데 등록할 때
    public ResponseData commuteUpdate(CommuteDTO.CommuteUpdateDTO commuteUpdateDTO) {
        // 등록일 + employee id 로 이미 있는 건지 조사
        Optional<Commute> findCommuteInfo =  commuteCustomRepository.findCommuteInfo(commuteUpdateDTO.getRegisterDate(), commuteUpdateDTO.getEmployeeId());

        if (findCommuteInfo.isEmpty()) {  // 등록
            Optional<Employee> findEmployee = employeeRepository.findById(commuteUpdateDTO.getEmployeeId());
            if (findEmployee.isEmpty()) return new ResponseData(Status.NOT_FOUND_EMPLOYEE, Status.NOT_FOUND_EMPLOYEE.getDescription());

            Commute newCommute = Commute.builder()
                    .employee(findEmployee.get())
                    .registerDate(commuteUpdateDTO.getRegisterDate())
                    .startDate(commuteUpdateDTO.getStartDate())
                    .endDate(commuteUpdateDTO.getEndDate())

                    .build();

            commuteRepository.save(newCommute);

        } else {    // 수정

            findCommuteInfo.get().updateStartDate(commuteUpdateDTO.getStartDate());
            findCommuteInfo.get().updateEndDate(commuteUpdateDTO.getEndDate());
            findCommuteInfo.get().updateRegisterDate(commuteUpdateDTO.getRegisterDate());

        }

        return new ResponseData(Status.COMMUTE_UPDATE_SUCCESS, Status.COMMUTE_UPDATE_SUCCESS.getDescription());
    }

    // 월별 출퇴근 조회 - 내꺼, 전체꺼
    // boolean expression
    public ResponseData commuteMy(UsernamePasswordAuthenticationToken authenticationToken, CommuteDTO.CommuteDateInfo commuteDateInfo) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        List<CommuteDTO.CommuteListBoard> commuteListBoardList = commuteCustomRepository.readCommute(authenticationDTO.getId(),
                Integer.valueOf(commuteDateInfo.getYear()),
                Integer.valueOf((commuteDateInfo.getMonth()))
        );

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteListBoardList);
    }

    public ResponseData commuteAll(CommuteDTO.CommuteDateInfo commuteDateInfo) {
        List<CommuteDTO.CommuteListBoard> commuteListBoardList = commuteCustomRepository.readCommute(null,
                Integer.valueOf(commuteDateInfo.getYear()),
                Integer.valueOf((commuteDateInfo.getMonth()))
        );

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteListBoardList);
    }


    // 출퇴근 현황 조회
    public ResponseData commuteStateList(String sort, String orderBy, String pageNumber, String pageSize, String startDate, String endDate, String name) {
        if (! sort.equals("name") && ! sort.equals("date")) sort = "name";
        if (! orderBy.equals("asc") && ! sort.equals("desc")) orderBy = "asc";


        try {
            Integer.parseInt(pageNumber);
        } catch (NumberFormatException e) {
            pageNumber = "1";
        }

        try {
            Integer.parseInt(pageSize);
        } catch (NumberFormatException e) {
            pageSize = "10";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

//        System.out.println(simpleDateFormat.format(endDate) + " " + simpleDateFormat.format(startDate));

        Date formatStartDate;
        Date formatEndDate;

        try {
            formatStartDate = simpleDateFormat.parse(startDate);
        } catch (IllegalArgumentException | ParseException e) {
            formatStartDate = new Date();
        }

        try {
            formatEndDate = simpleDateFormat.parse(endDate);
        } catch (IllegalArgumentException | ParseException e) {
            formatEndDate = new Date();
        }

        if (formatStartDate.compareTo(formatEndDate) == 1) {
            formatEndDate = new Date();
        }


        System.out.println(simpleDateFormat.format(formatStartDate) + " " + simpleDateFormat.format(formatEndDate));

        Pageable pageable = PageRequest.of(Integer.parseInt(pageNumber), Integer.valueOf(pageSize),
                Sort.by(Sort.Direction.fromString(orderBy), sort));

        List<CommuteDTO.CommuteState> commuteStateList = commuteCustomRepository.readCommuteState(pageable, formatStartDate, formatEndDate, name);

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), commuteStateList);
    }
}
