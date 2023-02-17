package com.gangnam.portal.service;

import com.gangnam.portal.domain.Department;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.dto.TeamDTO;
import com.gangnam.portal.repository.AffiliationRepository;
import com.gangnam.portal.repository.DepartmentRepository;
import com.gangnam.portal.repository.RanksRepository;
import com.gangnam.portal.repository.custom.TeamCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {
    private final TeamCustomRepository teamCustomRepository;
    private final AffiliationRepository affiliationRepository;
    private final DepartmentRepository departmentRepository;
    private final RanksRepository ranksRepository;

    // 소속 조회
    public ResponseData<List<TeamDTO.AffiliationNameDTO>> getAffiliation() {
        return new ResponseData(Status.READ_SUCCESS,
                Status.READ_SUCCESS.getDescription(),
                teamCustomRepository.findAffiliationDepartment());
    }

    // 부서 조회
    public ResponseData<Department> getDepartment(Long affiliationId) {
        return new ResponseData(Status.READ_SUCCESS,
                Status.READ_SUCCESS.getDescription(),
                departmentRepository.findByAffiliationId(affiliationId));
    }

    // 부서 목록 조회
    public ResponseData<List<TeamDTO.DepartmentNameDTO>> getDepartmentList() {
        return new ResponseData(Status.READ_SUCCESS,
                Status.READ_SUCCESS.getDescription(),
                teamCustomRepository.findDepartmentAffiliation());
    }


    // 직급 조회
    public ResponseData<List<TeamDTO.RankDTO>> getRanks() {

        return new ResponseData(Status.READ_SUCCESS,
                Status.READ_SUCCESS.getDescription(),
                teamCustomRepository.findAllRank());
    }

}
