package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Commute;

import java.util.Date;

public interface CommuteCustomRepository {
    Commute findCommute(Date date, Long employeeId);
}
