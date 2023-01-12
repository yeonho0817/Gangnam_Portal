package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.dto.CommuteDTO;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CommuteCustomRepository {
    Optional<Commute> isAlreadyStart(String date, Long employeeId);

    Optional<Commute> findLatestCommute(Long employeeId);

    Optional<Commute> findCommuteInfo(Date date, Long employeeId);

    List<CommuteDTO.CommuteListBoard> readCommute(Long employeeId, Integer year, Integer month);

    List<CommuteDTO.CommuteState> readCommuteState(Pageable pageable, Date startDate, Date endDate, String name);

}
