package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Employee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(value = "SELECT DISTINCT e FROM Employee e " +
            "JOIN FETCH e.employeeEmailList " +
            "WHERE e.id=:employeeId")
    Optional<Employee> findById(@Param(value = "employeeId") Long employeeId);

    @Query(value = "SELECT DISTINCT e FROM Employee e " +
            "JOIN FETCH e.employeeEmailList " +
            "WHERE e.employeeNo=:employeeNo")
    Optional<Employee> findByEmployeeNo(@Param(value = "employeeNo") Long employeeNo);

    @Query(value = "SELECT DISTINCT e FROM Employee e " +
            "JOIN FETCH e.employeeEmailList " +
            "WHERE e.phone=:phone")
    Optional<Employee> findByPhone(@Param(value = "Phone") String phone);
}
