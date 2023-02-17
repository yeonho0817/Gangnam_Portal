package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.TeamDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TeamCustomRepositoryImpl implements TeamCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QAffiliation qAffiliation = QAffiliation.affiliation;
    private final QDepartment qDepartment = QDepartment.department;
    private final QRanks qRanks = QRanks.ranks;

    @Override
    public List<TeamDTO.AffiliationNameDTO> findAffiliationDepartment() {
        List<Affiliation> affiliations = jpaQueryFactory.selectFrom(qAffiliation)

                .leftJoin(qAffiliation.departmentList, qDepartment).fetchJoin()

                .distinct()

                .fetch();

        return affiliations.stream()
                .filter(distinctByKey(m -> m.getId()))
                .map(affiliation -> TeamDTO.AffiliationNameDTO.builder()
                            .affiliationId(affiliation.getId())
                            .affiliationName(affiliation.getName().getName())
                            .departmentNameList(affiliation.getDepartmentList().stream()
                                    .map(department -> TeamDTO.DepartmentNameDTO.builder()
                                            .departmentId(department.getId())
                                            .departmentName(department.getName().getName())

                                            .build())
                                    .collect(Collectors.toList())
                            )
                        .build())

                .collect(Collectors.toList());
    }

    @Override
    public Optional<Department> findByDepartmentId(Long departmentId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qDepartment)

                .join(qDepartment.affiliation, qAffiliation).fetchJoin()

                .where(qDepartment.id.eq(departmentId))

                .fetchOne());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @Override
    public List<TeamDTO.DepartmentNameDTO> findDepartmentAffiliation() {
        return jpaQueryFactory.select(Projections.constructor(TeamDTO.DepartmentNameDTO.class,
                        qDepartment.id.as("departmentId"),
                        qDepartment.name.stringValue().as("departmentName"),
                        qDepartment.affiliation.id.as("affiliationId"),
                        qDepartment.affiliation.name.stringValue().as("affiliationName")
                ))

                .from(qDepartment)

                .join(qDepartment.affiliation, qAffiliation)
                .on(qDepartment.affiliation.id.eq(qAffiliation.id))

                .fetch();
    }

    @Override
    public List<TeamDTO.RankDTO> findAllRank() {
        return jpaQueryFactory.select(Projections.fields(TeamDTO.RankDTO.class,
                    qRanks.id.as("rankId"),
                    qRanks.name.stringValue().as("rankName")
                ))

                .from(qRanks)

                .fetch();
    }
}
