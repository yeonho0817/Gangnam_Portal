package com.gangnam.portal.repository.custom;


import com.gangnam.portal.dto.EmployeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EmployeeCustomRepository {

    Optional<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(Long id);

    List<EmployeeDTO.EmployeeNameList> readEmployeeNameList();
    
    
    // 인력조회
    Page<EmployeeDTO.HRInfoData> readHumanResource(Pageable pageable, String selectValue, String searchText);
    
    
    // 소속/부서 조회
    Page<EmployeeDTO.EmployeeSimpleInfo> readHumanResourceDepartment(Pageable pageable, String name, String affiliation, String department);

    List<EmployeeDTO.EmployeeSimpleInfo> findByNameOrderByIdAsc(String name);

}
