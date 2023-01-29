package com.gangnam.portal.repository;

import com.gangnam.portal.domain.FortuneEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FortuneEmployeeRepository extends JpaRepository<FortuneEmployee, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE fortune_employee", nativeQuery = true)
    void truncate();
}
