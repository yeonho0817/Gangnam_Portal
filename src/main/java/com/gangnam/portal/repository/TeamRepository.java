package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Department, Long> {
}
