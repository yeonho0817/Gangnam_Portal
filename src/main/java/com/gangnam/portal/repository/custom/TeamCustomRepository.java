package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Department;
import com.gangnam.portal.dto.TeamDTO;

import java.util.List;
import java.util.Optional;

public interface TeamCustomRepository {
    List<TeamDTO.AffiliationNameDTO> findAffiliationDepartment();

    Optional<Department> findByDepartmentId(Long departmentId);

    List<TeamDTO.DepartmentNameDTO> findDepartmentAffiliation();

    List<TeamDTO.RankDTO> findAllRank();
}
