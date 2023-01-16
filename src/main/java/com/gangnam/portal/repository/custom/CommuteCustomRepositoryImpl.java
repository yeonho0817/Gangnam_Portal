package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.Commute;
import com.gangnam.portal.domain.QCommute;
import com.gangnam.portal.domain.QEmployee;
import com.gangnam.portal.dto.CommuteDTO;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Optional<Commute> findLatestCommute(Long employeeId) {

        Commute commute = jpaQueryFactory.selectFrom(qCommute)

                .leftJoin(qCommute.employee, qEmployee).fetchJoin()

                .where(qEmployee.id.eq(employeeId))

                .orderBy(qCommute.registerDate.desc())

                .fetchFirst();

        return Optional.ofNullable(commute);
    }

    @Override
    public List<CommuteDTO.CommuteListBoard> readCommute(Long employeeId, Integer year, Integer month) {

        StringTemplate formatDay = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.registerDate
                , ConstantImpl.create("%d"));

        Calendar calendar= Calendar.getInstance();
        calendar.set(year, month-1,1, 0, 0, 0);

        Date startDate = new Date(calendar.getTimeInMillis());

        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(year, month-1,dayOfMonth, 23, 59, 59);

        Date endDate = new Date(calendar.getTimeInMillis());

        return jpaQueryFactory.select(Projections.fields(CommuteDTO.CommuteListBoard.class,
                            qCommute.id.as("commuteId"),
                            qEmployee.nameKr.as("nameKr"),
                            getRegisterDateStringTemplate().as("registerDate"),
                            formatDay.as("day"),
                            getStartDateStringTemplate().as("startDate"),
                            getEndDateStringTemplate().as("endDate")
                        ))
                .from(qCommute)

                .leftJoin(qCommute.employee, qEmployee)
                .on(qCommute.employee.id.eq(qEmployee.id))

                .where(eqEmployeeId(employeeId),
                        qCommute.registerDate.goe(startDate),
                        qCommute.registerDate.loe(endDate)
                )

                .fetch()
                ;
    }

    @Override
    public Page<CommuteDTO.CommuteStateData> readCommuteState(Pageable pageable,
                                                              Date startDate, Date endDate, String name) {

        List<CommuteDTO.CommuteStateData> commuteStateDataList = jpaQueryFactory.select(Projections.fields(CommuteDTO.CommuteStateData.class,
                        qCommute.id.as("commuteId"),
                        qEmployee.nameKr.as("nameKr"),
                        getRegisterDateStringTemplate().as("registerDate"),
                        getStartDateStringTemplate().as("startDate"),
                        getEndDateStringTemplate().as("endDate")
                ))
                .from(qCommute)

                .leftJoin(qCommute.employee, qEmployee)

                .where(
                        qCommute.registerDate.goe(startDate),
                        qCommute.registerDate.loe(endDate),
                        containsName(name)
                )

                .orderBy(commuteSort(pageable))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch()
                ;

        return new PageImpl(commuteStateDataList, pageable, getTotalPage(startDate, endDate, name));
    }

    private BooleanExpression eqEmployeeId(Long employeeId) {
        if (employeeId == null) return null;

        return qEmployee.id.eq(employeeId);
    }


    public Long getTotalPage(Date startDate, Date endDate, String name) {
        return jpaQueryFactory.select(qCommute.count())
                .from(qCommute)

                .leftJoin(qCommute.employee, qEmployee)

                .where(
                        qCommute.registerDate.goe(startDate),
                        qCommute.registerDate.loe(endDate),
                        containsName(name)
                )

                .fetchOne()
                ;
    }

    // x.goe(y) : x >= y
    // x.gt(y) : x > y
    // x.loe(y) : x <= y
    // x.lt(y) : x < y

    private StringTemplate getRegisterDateStringTemplate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.registerDate
                , ConstantImpl.create("%Y-%m-%d"));
    }

    private StringTemplate getStartDateStringTemplate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.startDate
                , ConstantImpl.create("%Y-%m-%d %H:%i:%s"));
    }

    private StringTemplate getEndDateStringTemplate() {
        return Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                , qCommute.endDate
                , ConstantImpl.create("%Y-%m-%d %H:%i:%s"));
    }

    private BooleanExpression containsName(String name) {
        if (name == null) {
            return null;
        }
        return qEmployee.nameKr.contains(name);
    }

//    https://velog.io/@seungho1216/Querydsl%EB%8F%99%EC%A0%81-sorting%EC%9D%84-%EC%9C%84%ED%95%9C-OrderSpecifier-%ED%81%B4%EB%9E%98%EC%8A%A4-%EA%B5%AC%ED%98%84
    private OrderSpecifier<String> commuteSort(Pageable pageable) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!pageable.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : pageable.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()){
                    case "name":
                        return new OrderSpecifier(direction, qEmployee.nameKr);
                    case "date":
                        return new OrderSpecifier(direction, qCommute.registerDate);
                }
            }
        }
        return null;
    }

}
