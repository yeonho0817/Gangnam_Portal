package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Commute;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class CommuteCustomRepositoryImpl implements CommuteCustomRepository {
    @Override
    public Commute findCommute(Date date, Long employeeId) {
        return null;
    }
}
