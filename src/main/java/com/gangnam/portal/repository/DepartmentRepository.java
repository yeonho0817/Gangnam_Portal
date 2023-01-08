package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Affiliation, Long> {
}
