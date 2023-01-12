package com.gangnam.portal.service;

import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.QueryConditionDTO;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HumanResourceService {
    private final EmployeeCustomRepository employeeCustomRepository;

    // 인력 조회
    public ResponseData findHumanResource(String sort, String orderBy, String pageSize, String pageNumber, String selectValue, String searchText) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));

        List<EmployeeDTO.HRInfo> hrInfoList = employeeCustomRepository.readHumanResource(pageable, selectValue, searchText);

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), hrInfoList);
    }

    // 소속/부서 조회
    public ResponseData findHumanResourceDept(String sort, String orderBy, String pageSize, String pageNumber, String name, String affiliation, String department) {
        QueryConditionDTO queryConditionDTO = new QueryConditionDTO(sort, orderBy, pageNumber, pageSize);

        Pageable pageable = PageRequest.of(queryConditionDTO.getPageNumber(), queryConditionDTO.getPageSize(),
                Sort.by(Sort.Direction.fromString(queryConditionDTO.getOrderBy()), queryConditionDTO.getSort()));


        List<EmployeeDTO.HRDepartmentInfo> hrDepartmentInfoList = employeeCustomRepository.readHumanResourceDepartment(pageable, name, affiliation, department);

        return new ResponseData(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), hrDepartmentInfoList);
    }
}
