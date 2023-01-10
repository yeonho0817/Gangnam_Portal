package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.EmployeeDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(Long id) {
        QEmployee qEmployee = QEmployee.employee;
        QEmployeeEmail qEmployeeEmail = QEmployeeEmail.employeeEmail;
        QDepartment qDepartment = QDepartment.department;
        QRanks qRanks = QRanks.ranks;
        QAffiliation qAffiliation = QAffiliation.affiliation;



        return null;
    }
}
