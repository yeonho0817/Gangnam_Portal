package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
    @Query(value = "SELECT a FROM Affiliation a JOIN FETCH a.departmentList")
    List<Affiliation> findAll();
}
