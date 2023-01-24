package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.EmployeeDTO;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.querydsl.core.group.GroupBy.groupBy;

@Repository
@RequiredArgsConstructor
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    private final QEmployee qEmployee = QEmployee.employee;
    private final QEmployeeEmail qEmployeeEmail = QEmployeeEmail.employeeEmail;
    private final QDepartment qDepartment = QDepartment.department;
    private final QRanks qRanks = QRanks.ranks;
    private final QAffiliation qAffiliation = QAffiliation.affiliation;

    @Override
    public Optional<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(Long id) {

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
                    qDepartment.name.as("department"),
                    qEmployee.state.stringValue().as("state")
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
        return jpaQueryFactory.select(Projections.fields(EmployeeDTO.EmployeeNameList.class,
                    qEmployee.id.as("employeeId"),
                    qEmployee.nameKr.as("nameKr")
                ))
                .from(qEmployee)
                
                .where(qEmployee.state.eq(false)) // false = 0 재직인 사람들
                .fetch()
                ;
    }

//    @Override
//    public Page<EmployeeDTO.HRInfoData> readHumanResource(Pageable pageable, String selectValue, String searchText) {
//        // selectValue = 이름, 직급, 소속
//        // searchText = 검색어
//
//        Map<Long, EmployeeDTO.HRInfoData> hrInfoList = jpaQueryFactory.selectFrom(qEmployee)
//
//                .leftJoin(qEmployee.ranks, qRanks).on(qEmployee.ranks.id.eq(qRanks.id))
//                .leftJoin(qEmployee.affiliation, qAffiliation).on(qEmployee.affiliation.id.eq(qAffiliation.id))
//                .leftJoin(qEmployee.department, qDepartment).on(qEmployee.department.id.eq(qDepartment.id))
//                .leftJoin(qEmployee.employeeEmailList, qEmployeeEmail).on(qEmployee.id.eq(qEmployeeEmail.employee.id))
//
//                .where(containsSearchText(selectValue, searchText),
//                        qEmployee.state.eq(false))
//
//                .orderBy(humanResourceSort(pageable))
//
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//
//                .transform(
//                        groupBy(qEmployee.id).as(
//                                new EmployeeDTO.HRInfoData(
//
//                                        qEmployee.id.as("employeeId"),
//                                        qEmployee.nameKr.as("nameKr"),
//                                        qRanks.name.stringValue().as("rank"),
//                                        qAffiliation.name.stringValue().as("affiliation"),
//                                        qDepartment.name.stringValue().as("department"),
//                                        qEmployee.phone.as("phone"),
//
//                                        set( qEmployeeEmail.email.toString())
//                                ))
//                        )
//                ))
//                ;
//
//
//        return new PageImpl<>(hrInfoList.keySet().stream().map(hrInfoList::get).collect(Collectors.toList()), pageable, getHRTotalPage(selectValue, searchText));
//    }

    @Override
    public Page<EmployeeDTO.HRInfoData> readHumanResource(Pageable pageable, String selectValue, String searchText) {
        // selectValue = 이름, 직급, 소속
        // searchText = 검색어

        List<Employee> hrInfoList = jpaQueryFactory.selectFrom(qEmployee)

                .leftJoin(qEmployee.ranks, qRanks).fetchJoin()
                .leftJoin(qEmployee.affiliation, qAffiliation).fetchJoin()
                .leftJoin(qEmployee.department, qDepartment).fetchJoin()

                .where(containsSearchText(selectValue, searchText),
                        qEmployee.state.eq(false))

                .orderBy(humanResourceSort(pageable))

                .groupBy(qEmployee.id)

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        List<EmployeeDTO.HRInfoData> hrInfoDataList = hrInfoList.stream()
                .map(employee -> EmployeeDTO.HRInfoData.builder()
                        .employeeId(employee.getId())
                        .nameKr(employee.getNameKr())
                        .rank(employee.getRanks().getName().name())
                        .affiliation(employee.getAffiliation().getName().name())
                        .department(employee.getDepartment().getName().name())
                        .phone(employee.getPhone())
                        .email(employee.getEmployeeEmailList())

                        .build()

                ).collect(Collectors.toList());

        return new PageImpl<>(hrInfoDataList, pageable, getHRTotalPage(selectValue, searchText));
    }

    private Long getHRTotalPage(String selectValue, String searchText) {
        return jpaQueryFactory.selectDistinct(qEmployee.id.count())
                .from(qEmployee)

                .where(containsSearchText(selectValue, searchText),
                        qEmployee.state.eq(false))

                .fetchOne()
                ;

    }


    private OrderSpecifier<String> humanResourceSort(Pageable pageable) {
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
                    case "rank":
                        return new OrderSpecifier(direction, qEmployee.ranks.id);
                }
            }
        }
        return null;
    }

    private BooleanExpression containsSearchText(String selectValue, String searchText) {
        if (selectValue == null || searchText == null) return null;

        switch (selectValue) {
            case "이름": case "nameKr":
                return containsName(searchText);
            case "직급": case "rank":
                return qRanks.name.stringValue().contains(searchText);
            case "소속": case "affiliation":
                return qAffiliation.name.stringValue().contains(searchText);
            default:
                return null;
        }
    }

    @Override
    public Page<EmployeeDTO.EmployeeSimpleInfo> readHumanResourceDepartment(Pageable pageable, String name, String affiliation, String department) {

        List<EmployeeDTO.EmployeeSimpleInfo> hrDepartmentInfoList = jpaQueryFactory.selectFrom(qEmployee)

                .join(qEmployee.department, qDepartment)
                .on(qEmployee.department.id.eq(qDepartment.id))

                .join(qEmployee.ranks, qRanks)
                .on(qEmployee.ranks.id.eq(qRanks.id))

                .join(qEmployee.affiliation, qAffiliation)
                .on(qEmployee.affiliation.id.eq(qAffiliation.id))

                .where(containsName(name),
                        eqAffiliation(affiliation),
                        eqDepartment(department),
                        qEmployee.state.eq(false)
                )

                .orderBy(humanResourceSort(pageable))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .transform(
                        groupBy(qEmployee.id).list(
                                Projections.fields(EmployeeDTO.EmployeeSimpleInfo.class,
                                        qEmployee.id.as("employeeId"),
                                        qEmployee.employeeNo.as("employeeNo"),
                                        qEmployee.nameKr.as("nameKr"),
                                        qRanks.name.stringValue().as("rank"),
                                        qAffiliation.name.stringValue().as("affiliation"),
                                        qDepartment.name.stringValue().as("department")
                                ))
                );


        return new PageImpl<>(hrDepartmentInfoList, pageable, getTeamTotalPage(name, affiliation, department));
    }

    @Override
    public List<EmployeeDTO.EmployeeSimpleInfo> findAllOrderByIdAsc(String name) {
//        OrderSpecifier orderSpecifier = new OrderSpecifier(Order.ASC, qEmployee.id);

        return jpaQueryFactory.select(Projections.fields(EmployeeDTO.EmployeeSimpleInfo.class,
                    qEmployee.id.as("employeeId"),
                    qEmployee.employeeNo.as("employeeNo"),
                    qEmployee.nameKr.as("nameKr"),
                    qRanks.name.stringValue().as("rank"),
                    qAffiliation.name.stringValue().as("affiliation"),
                    qDepartment.name.stringValue().as("department")
                ))
                .from(qEmployee)

                .join(qEmployee.ranks, qRanks)
                .on(qEmployee.ranks.id.eq(qRanks.id))

                .join(qEmployee.affiliation, qAffiliation)
                .on(qEmployee.affiliation.id.eq(qAffiliation.id))

                .join(qEmployee.department, qDepartment)
                .on(qEmployee.department.id.eq(qDepartment.id))

                .where(containsName(name),
                        qEmployee.state.eq(false)
                )

                .orderBy(new OrderSpecifier(Order.ASC, qEmployee.id))

                .fetch()
                ;
    }

    private Long getTeamTotalPage(String name, String affiliation, String department) {
        return jpaQueryFactory.select(qEmployee.count())
                .from(qEmployee)

                .leftJoin(qEmployee.department, qDepartment)
                .on(qEmployee.department.id.eq(qDepartment.id))

                .join(qEmployee.affiliation, qAffiliation)
                .on(qEmployee.affiliation.id.eq(qAffiliation.id))

                .where(containsName(name),
                        eqAffiliation(affiliation),
                        eqDepartment(department),
                        qEmployee.state.eq(false)
                )

                .distinct()

                .fetchOne()
                ;

    }

    private BooleanExpression containsName(String name) {
        if (name == null) return null;

        return qEmployee.nameKr.contains(name);
    }

    private BooleanExpression eqAffiliation(String affiliation) {
        if (affiliation == null || affiliation.equals("")) return null;

        return qAffiliation.name.stringValue().eq(affiliation);
    }

    private BooleanExpression eqDepartment(String department) {
        if (department == null || department.equals("")) return null;

        return qDepartment.name.stringValue().eq(department);
    }



}
