package com.gangnam.portal.repository.custom;

import com.gangnam.portal.dto.EtcDTO;

import java.util.Optional;

public interface FortuneEmployeeCustomRepository {
    Optional<EtcDTO.FortuneDTO> findByEmployeeId(Long employeeId);
}
