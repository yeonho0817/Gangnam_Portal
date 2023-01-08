package com.gangnam.portal.repository;

import com.gangnam.portal.domain.EmployeeEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeEmailRepository extends JpaRepository<EmployeeEmail, Long> {

}
