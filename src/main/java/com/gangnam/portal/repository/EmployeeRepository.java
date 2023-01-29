package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(Long employeeId);
}
