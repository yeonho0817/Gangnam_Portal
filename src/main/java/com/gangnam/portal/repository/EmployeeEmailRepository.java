package com.gangnam.portal.repository;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.domain.Provider;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeEmailRepository extends JpaRepository<EmployeeEmail, Long> {
    Optional<EmployeeEmail> findByEmployeeIdAndProvider(Long employeeId, Provider provider);

    @Query(value = "SELECT ee FROM EmployeeEmail ee " +
            "JOIN FETCH ee.employee " +
            "WHERE ee.email=:email AND ee.provider=:provider")
    Optional<EmployeeEmail> findByEmailAndProviderWithEmployee(@Param(value = "email") String email, @Param(value = "provider") Provider provider);

    List<EmployeeEmail> findByEmployeeId(Long employeeId);
}
