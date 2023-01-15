package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.QAffiliation;
import com.gangnam.portal.domain.QDepartment;
import com.gangnam.portal.dto.TeamDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class TeamCustomRepositoryImpl implements TeamCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QAffiliation qAffiliation = QAffiliation.affiliation;
    private final QDepartment qDepartment = QDepartment.department;

    @Override
    public List<TeamDTO.AffiliationNameDTO> findAffiliationDepartment() {

        List<TeamDTO.AffiliationNameDTO> affiliationList = jpaQueryFactory.selectFrom(qAffiliation)

                .leftJoin(qAffiliation.departmentList, qDepartment)
                .on(qAffiliation.id.eq(qDepartment.affiliation.id))

                .transform(
                    groupBy(qAffiliation.id).list(
                        Projections.fields(TeamDTO.AffiliationNameDTO.class,
                            qAffiliation.id.as("affiliationId"),
                            qAffiliation.name.stringValue().as("affiliationName"),
                            list(
                                    Projections.fields(
                                            TeamDTO.DepartmentNameDTO.class,
                                            qDepartment.id.as("departmentId"),
                                            qDepartment.name.stringValue().as("departmentName")
                                    )
                            ).as("departmentNameDTOList")
                    ))
                );


        return affiliationList;
    }
}
