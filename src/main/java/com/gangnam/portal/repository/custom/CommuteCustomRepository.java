package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.dto.CommuteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CommuteCustomRepository {
    Optional<Commute> findLatestCommute(Long employeeId);

    List<CommuteDTO.CommuteListBoard> readCommute(Long employeeId, Integer year, Integer month);

    Page<CommuteDTO.CommuteStateData> readCommuteState(Pageable pageable, Date startDate, Date endDate, Long employeeId);

    List<CommuteDTO.CommuteExcelData> findCommuteExcel(Date startDate, Date endDate, Long employeeId);

    Optional<Commute> findCommute(Long employeeId, Date registerDate);



}
