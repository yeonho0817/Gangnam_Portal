package com.gangnam.portal.service;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.QueryConditionDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.dto.TeamDTO;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import com.gangnam.portal.repository.custom.TeamCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HumanResourceService {
    private final EmployeeCustomRepository employeeCustomRepository;
    private final TeamCustomRepository teamCustomRepository;
    private final EmployeeRepository employeeRepository;

    // 소속/부서 이름 조회
    @Transactional(readOnly = true)
    public ResponseData<List<TeamDTO.AffiliationNameDTO>> findAffiliationDepartment() {
        List<TeamDTO.AffiliationNameDTO> affiliationNameDTOList = teamCustomRepository.findAffiliationDepartment();

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), affiliationNameDTOList);
    }

    // 인력 조회
    @Transactional(readOnly = true)
    public ResponseData<EmployeeDTO.HRInfo> findHumanResource(String sort, String orderBy, String pageSize, String pageNumber, String selectValue, String searchText) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));

        Page<EmployeeDTO.HRInfoData> hrInfoList = employeeCustomRepository.readHumanResource(pageable, selectValue, searchText);

        List<EmployeeDTO.HRInfoDataList> hrInfoDataLists = hrInfoList.stream()
                .map(employee -> EmployeeDTO.HRInfoDataList.builder()
                        .employeeId(employee.getEmployeeId())
                        .nameKr(employee.getNameKr())
                        .rank(employee.getRank())
                        .affiliation(employee.getAffiliation())
                        .department(employee.getDepartment())
                        .phone(employee.getPhone())
                        .email(
                                employee.getEmail().stream()
                                        .map(EmployeeEmail::getEmail)
                                        .collect(Collectors.joining(",")))
                        .build())
                .collect(Collectors.toList());

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EmployeeDTO.HRInfo(hrInfoList.getTotalPages(), (int) hrInfoList.getTotalElements(), hrInfoDataLists));
    }

    // 소속/부서 조회
    @Transactional(readOnly = true)
    public ResponseData<EmployeeDTO.HRDepartmentInfo> findHumanResourceDept(String sort, String orderBy, String pageSize, String pageNumber, String name, String affiliation, String department) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));


        Page<EmployeeDTO.EmployeeSimpleInfo> hrDepartmentInfoList = employeeCustomRepository.readHumanResourceDepartment(pageable, name, affiliation, department);

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EmployeeDTO.HRDepartmentInfo(hrDepartmentInfoList.getTotalPages(), (int) hrDepartmentInfoList.getTotalElements(), hrDepartmentInfoList.stream().collect(Collectors.toList())));
    }
}
