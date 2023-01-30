package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.QEmployee;
import com.gangnam.portal.domain.QFortune;
import com.gangnam.portal.domain.QFortuneEmployee;
import com.gangnam.portal.dto.EtcDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FortuneEmployeeCustomRepositoryImpl implements FortuneEmployeeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QEmployee qEmployee = QEmployee.employee;
    private final QFortune qFortune = QFortune.fortune;
    private final QFortuneEmployee qFortuneEmployee = QFortuneEmployee.fortuneEmployee;

    @Override
    public Optional<EtcDTO.FortuneDTO> findByEmployeeId(Long employeeId) {
        EtcDTO.FortuneDTO findFortuneMessage = jpaQueryFactory.select(Projections.fields(EtcDTO.FortuneDTO.class,
                    qFortune.message.as("message")
                ))
                .from(qFortuneEmployee)

                .join(qFortuneEmployee.fortune, qFortune)
                .on(qFortuneEmployee.fortune.id.eq(qFortune.id))

                .join(qFortuneEmployee.employee, qEmployee)
                .on(qFortuneEmployee.employee.id.eq(qEmployee.id))

                .where(qFortuneEmployee.employee.id.eq(employeeId))

                .fetchOne();

        return Optional.ofNullable(findFortuneMessage);
    }
}
