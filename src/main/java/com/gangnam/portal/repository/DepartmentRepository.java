package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByAffiliationId(Long affiliationId);
}
