package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.domain.QCommute;
import com.gangnam.portal.domain.QEmployee;
import com.gangnam.portal.dto.CommuteDTO;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommuteCustomRepositoryImpl implements CommuteCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final QCommute qCommute = QCommute.commute;
    private final QEmployee qEmployee = QEmployee.employee;
    @Override
    public Optional<Commute> isAlreadyStart(String date, Long employeeId) {
        StringTemplate formatDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.registerDate
                , ConstantImpl.create("%Y-%m-%d"));

        Commute commute = jpaQueryFactory.selectFrom(qCommute)

                .leftJoin(qCommute.employee, qEmployee).fetchJoin()

                .where(formatDate.eq(date)
                        .and(qEmployee.id.eq(employeeId)))

                .fetchOne();


        return Optional.ofNullable(commute);
    }

    @Override
    public Optional<Commute> findLatestCommute(Long employeeId) {
        Commute commute = jpaQueryFactory.selectFrom(qCommute)

                .leftJoin(qCommute.employee, qEmployee).fetchJoin()

                .where(qEmployee.id.eq(employeeId))

                .orderBy(qCommute.registerDate.desc())

                .fetchOne();

        return Optional.ofNullable(commute);
    }

    @Override
    public Optional<Commute> findCommuteInfo(Date date, Long employeeId) {

        StringTemplate formatDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.registerDate
                , ConstantImpl.create("%Y-%m-%d"));

        Commute commute = jpaQueryFactory.selectFrom(qCommute)

                .leftJoin(qCommute.employee, qEmployee).fetchJoin()

                .where(
                        qEmployee.id.eq(employeeId)
                        .and(qCommute.registerDate.eq(date))
                )

                .fetchOne();

        return Optional.ofNullable(commute);
    }

    @Override
    public List<CommuteDTO.CommuteListBoard> readCommute(Long employeeId, Integer year, Integer month) {

        StringTemplate formatDay = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.registerDate
                , ConstantImpl.create("%d"));

        Date startDate = new Date(year, month, 1);

        Calendar calendar= Calendar.getInstance();
        calendar.set(year, month-1,1);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date endDate = new Date(year, month, dayOfMonth);


        return jpaQueryFactory.select(Projections.fields(CommuteDTO.CommuteListBoard.class,
                            qEmployee.nameKr.as("nameKr"),
                            qCommute.registerDate.as("registerDate"),
                            formatDay.as("day"),
                            qCommute.startDate.as("startDate"),
                            qCommute.endDate.as("endDate")
                        ))
                .from(qCommute)

                .leftJoin(qCommute.employee, qEmployee)

                .where(eqEmployeeId(employeeId).and(
                        qCommute.registerDate.between(startDate, endDate)
                ))

                .fetch()
                ;
    }

    private BooleanExpression eqEmployeeId(Long employeeId) {
        if (employeeId == null) {
            return null;
        }
        return qEmployee.id.eq(employeeId);
    }

    @Override
    public List<CommuteDTO.CommuteStateList> readCommuteState(String sort, String oderBy,
                                                              String pageNumber, String pageSize,
                                                              Date startDate, Date endDate, String name) {
        StringTemplate formatRegisterDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.registerDate
                , ConstantImpl.create("%Y-%m-%d"));

        StringTemplate formatStartDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.startDate
                , ConstantImpl.create("%Y-%m-%d"));

        StringTemplate formatEndDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.endDate
                , ConstantImpl.create("%Y-%m-%d"));


        return jpaQueryFactory.select(Projections.fields(CommuteDTO.CommuteStateList.class,
                        qEmployee.nameKr.as("nameKr"),
                        formatRegisterDate.as("registerDate"),
                        formatStartDate.as("startDate"),
                        formatEndDate.as("endDate")
                    ))
                .from(qCommute)

                .leftJoin(qCommute.employee, qEmployee)

                .where(
                        goeStartDate(startDate),
                        loeEndDate(endDate),
                        containsName(name)
                )

                .orderBy()

                // 페이징 처리
                .fetch()
                ;
    }

    // x.goe(y) : x >= y
    // x.gt(y) : x > y
    // x.loe(y) : x <= y
    // x.lt(y) : x < y
    private BooleanExpression goeStartDate(Date startDate) {
        if (startDate == null) {
            return null;
        }
        return qCommute.registerDate.goe(startDate);
    }

    private BooleanExpression loeEndDate(Date endDate) {
        if (endDate == null) {
            return null;
        }
        return qCommute.registerDate.loe(endDate);
    }

    private BooleanExpression containsName(String name) {
        if (name == null) {
            return null;
        }
        return qEmployee.nameKr.contains(name);
    }

//    https://velog.io/@seungho1216/Querydsl%EB%8F%99%EC%A0%81-sorting%EC%9D%84-%EC%9C%84%ED%95%9C-OrderSpecifier-%ED%81%B4%EB%9E%98%EC%8A%A4-%EA%B5%AC%ED%98%84
//    private OrderSpecifier<String> orderBy(String sort, String orderBy) {
//        if (sort.equals("name")) {
//            if (orderBy.equals("asc")) return qEmployee.nameKr.asc();
//            else return qEmployee.nameKr.desc();
//        } else { //(sort.equals("date"))
//            if (orderBy.equals("asc")) return qCommute.registerDate.asc();
//            else return qEmployee.nameKr.desc();
//        }
//    }

}
