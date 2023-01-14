package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.QAffiliation;
import com.gangnam.portal.domain.QDepartment;
import com.gangnam.portal.dto.TeamDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamCustomRepositoryImpl implements TeamCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QAffiliation qAffiliation = QAffiliation.affiliation;
    private final QDepartment qDepartment = QDepartment.department;

    @Override
    public List<TeamDTO.AffiliationNameDTO> findAffiliationDepartment() {

        return jpaQueryFactory.select(Projections.fields(TeamDTO.AffiliationNameDTO.class,
                        qAffiliation.id.as("affiliationId")
                ))
                .from(qAffiliation)

                .fetch()
                ;
    }
}
