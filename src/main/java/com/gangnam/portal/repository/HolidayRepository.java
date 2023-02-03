package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
//    List<Holiday> findBy
}
