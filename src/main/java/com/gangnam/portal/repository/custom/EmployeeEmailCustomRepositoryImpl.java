package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmployeeEmailCustomRepositoryImpl implements EmployeeEmailCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<EmployeeEmail> isExists(String email, String provider) {
        Provider enumProvider = Enum.valueOf(Provider.class, provider);

        QEmployeeEmail qEmployeeEmail = QEmployeeEmail.employeeEmail;
        QEmployee qEmployee = QEmployee.employee;
        QAuthority qAuthority = QAuthority.authority;

        EmployeeEmail employeeEmail = jpaQueryFactory
                .selectFrom(qEmployeeEmail)

                .leftJoin(qEmployeeEmail.employee, qEmployee).fetchJoin()

                .leftJoin(qEmployee.authority, qAuthority).fetchJoin()

                .where(qEmployeeEmail.email.eq(email)
                        .and(qEmployeeEmail.provider.eq(enumProvider))
                )
                .fetchOne();

        return Optional.ofNullable(employeeEmail);
    }

}
