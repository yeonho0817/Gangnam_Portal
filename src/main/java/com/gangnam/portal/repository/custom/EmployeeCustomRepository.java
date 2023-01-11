package com.gangnam.portal.repository.custom;


import com.gangnam.portal.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeCustomRepository {

    Optional<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(Long id);

    List<EmployeeDTO.EmployeeNameList> readEmployeeNameList();

}
