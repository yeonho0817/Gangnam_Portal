package com.gangnam.portal.service;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.QueryConditionDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.dto.TeamDTO;
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

    // 소속/부서 이름 조회
    public ResponseData findAffiliationDepartment() {
        List<TeamDTO.AffiliationNameDTO> affiliationNameDTOList = teamCustomRepository.findAffiliationDepartment();

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), affiliationNameDTOList);
    }

    // 인력 조회
    public ResponseData findHumanResource(String sort, String orderBy, String pageSize, String pageNumber, String selectValue, String searchText) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));

        Page<EmployeeDTO.HRInfoData> hrInfoList = employeeCustomRepository.readHumanResource(pageable, selectValue, searchText);

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EmployeeDTO.HRInfo(hrInfoList.getTotalPages(), hrInfoList.stream().collect(Collectors.toList())));
    }

    // 소속/부서 조회
    public ResponseData findHumanResourceDept(String sort, String orderBy, String pageSize, String pageNumber, String name, String affiliation, String department) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));


        Page<EmployeeDTO.HRDepartmentInfoData> hrDepartmentInfoList = employeeCustomRepository.readHumanResourceDepartment(pageable, name, affiliation, department);



        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), new EmployeeDTO.HRDepartmentInfo(hrDepartmentInfoList.getTotalPages(), hrDepartmentInfoList.stream().collect(Collectors.toList())));
    }
}
