package com.gangnam.portal.repository.custom;

import com.gangnam.portal.dto.FortuneDTO;

import java.util.Optional;

public interface FortuneEmployeeCustomRepository {
    Optional<FortuneDTO> findByEmployeeId(Long employeeId);
}
