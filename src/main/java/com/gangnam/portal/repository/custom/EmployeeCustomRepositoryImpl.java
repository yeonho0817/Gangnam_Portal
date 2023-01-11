package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.EmployeeDTO;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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

        // 입사일 포맷
        StringTemplate formatJoinDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qEmployee.joinDate
                , ConstantImpl.create("%Y-%m-%d"));

        // 생일 포맷
        StringTemplate formatBirthday = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qEmployee.birthday
                , ConstantImpl.create("%Y-%m-%d"));


        EmployeeDTO.EmployeeInfoDTO employeeInfoDTO = jpaQueryFactory.select(Projections.fields(EmployeeDTO.EmployeeInfoDTO.class,
                    qEmployee.id.as("employeeId"),
                    qEmployee.employeeNo.as("employeeNo"),
                    qEmployee.nameKr.as("nameKr"),
                    qEmployee.nameEn.as("nameEn"),
                    formatBirthday.as("birthday"),
                    qEmployee.profileImg.as("profileImg"),
                    qEmployee.gender.as("gen"),
                    qEmployee.phone.as("phone"),
                    qEmployee.address.as("address"),
                    formatJoinDate.as("joinDate"),
                    qRanks.name.as("rank"),
                    qAffiliation.name.as("affiliation"),
                    qDepartment.name.as("department")
                ))
                .from(qEmployee)

                .leftJoin(qEmployee.department, qDepartment)
                .leftJoin(qEmployee.ranks, qRanks)
                .leftJoin(qEmployee.affiliation, qAffiliation)

                .where(qEmployee.id.eq(id))

                .fetchOne();



        return Optional.ofNullable(employeeInfoDTO);
    }

    @Override
    public List<EmployeeDTO.EmployeeNameList> readEmployeeNameList() {
        QEmployee qEmployee = QEmployee.employee;

        return jpaQueryFactory.select(Projections.fields(EmployeeDTO.EmployeeNameList.class,
                    qEmployee.id.as("employeeId"),
                    qEmployee.nameKr.as("nameKr")
                ))
                .from(qEmployee)
                
                .where(qEmployee.state.eq(false)) // false = 0 재직인 사람들
                .fetch()
                ;
    }
}
