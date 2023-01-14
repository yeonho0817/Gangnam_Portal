package com.gangnam.portal.repository.custom;

import com.gangnam.portal.dto.TeamDTO;

import java.util.List;

public interface TeamCustomRepository {
    List<TeamDTO.AffiliationNameDTO> findAffiliationDepartment();
}
