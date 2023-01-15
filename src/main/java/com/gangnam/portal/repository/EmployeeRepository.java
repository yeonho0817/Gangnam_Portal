package com.gangnam.portal.repository;

import com.gangnam.portal.domain.Employee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Query("UPDATE Employee e SET e.nameEn=:nameEn, e.phone=:phone, e.address=:address WHERE id=:id")
    void updateEmployeeInfo(@Param("id") Long id, @Param("nameEn") String nameEn, @Param("phone") String phone, @Param("address") String address);

}
