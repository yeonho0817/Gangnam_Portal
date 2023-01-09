package com.gangnam.portal.repository.custom;

import com.gangnam.portal.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeCustomRepository {

    List<EmployeeDTO.ReadHumanResource> readHumanResource(String sort, String orderBy, String selectValue, String searchTxt, Integer pageSize, Integer pageNumber);

}
