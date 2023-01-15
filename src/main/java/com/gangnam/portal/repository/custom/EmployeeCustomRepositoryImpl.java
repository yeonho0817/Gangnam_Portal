package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.EmployeeDTO;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return jpaQueryFactory.select(Projections.fields(EmployeeDTO.EmployeeNameList.class,
                    qEmployee.id.as("employeeId"),
                    qEmployee.nameKr.as("nameKr")
                ))
                .from(qEmployee)
                
                .where(qEmployee.state.eq(false)) // false = 0 재직인 사람들
                .fetch()
                ;
    }

    @Override
    public Page<EmployeeDTO.HRInfoData> readHumanResource(Pageable pageable, String selectValue, String searchText) {
        // selectValue = 이름, 직급, 소속
        // searchText = 검색어
        
        // 이메일 id 젤 빠른거 한개만
        List<EmployeeDTO.HRInfoData> hrInfoList = jpaQueryFactory.select(Projections.fields(EmployeeDTO.HRInfoData.class,
                        qEmployee.id.as("employeeId"),
                        qEmployee.nameKr.as("nameKr"),
                        qRanks.name.stringValue().as("rank"),
                        qAffiliation.name.stringValue().as("affiliation"),
                        qDepartment.name.stringValue().as("department"),
                        qEmployee.phone.as("phone"),

                        ExpressionUtils.as(
                                JPAExpressions.select(qEmployeeEmail.email)
                                        .from(qEmployeeEmail)
                                        .leftJoin(qEmployeeEmail.employee, qEmployee)
                                        .on(qEmployeeEmail.employee.id.eq(qEmployee.id))
                                        .where(qEmployeeEmail.id.eq(qEmployee.id)),
                                "email")
                ))
                .from(qEmployee)

                .leftJoin(qEmployee.ranks, qRanks)
                .leftJoin(qEmployee.affiliation, qAffiliation)
                .leftJoin(qEmployee.department, qDepartment)

                .where(containsSearchText(selectValue, searchText),
                        qEmployee.state.eq(false))

                .orderBy(humanResourceSort(pageable))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        return new PageImpl<>(hrInfoList, pageable, getHRTotalPage(selectValue, searchText));
    }

    private Long getHRTotalPage(String selectValue, String searchText) {
        return jpaQueryFactory.select(qEmployee.count())
                .from(qEmployee)

                .leftJoin(qEmployee.ranks, qRanks)
                .leftJoin(qEmployee.affiliation, qAffiliation)
                .leftJoin(qEmployee.department, qDepartment)

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

                System.out.println(order.getProperty() + " " + order.getDirection().name());
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()){
                    case "name":
                        return new OrderSpecifier(direction, qEmployee.nameKr);
                    case "rank":
                        return new OrderSpecifier(direction, qRanks.id);
                }
            }
        }
        return null;
    }

    private BooleanExpression containsSearchText(String selectValue, String searchText) {
        if (selectValue == null || searchText == null) return null;

        switch (selectValue) {
            case "이름": case "name":
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
    public Page<EmployeeDTO.HRDepartmentInfoData> readHumanResourceDepartment(Pageable pageable, String name, String affiliation, String department) {

        List<EmployeeDTO.HRDepartmentInfoData> hrDepartmentInfoList = jpaQueryFactory.select(Projections.fields(EmployeeDTO.HRDepartmentInfoData.class,
                        qEmployee.id.as("employeeId"),
                        qEmployee.nameKr.as("nameKr"),
                        qRanks.name.as("rank"),
                        qAffiliation.name.as("affiliation"),
                        qDepartment.name.as("department")
                ))
                .from(qEmployee)

                .leftJoin(qEmployee.department, qDepartment)
                .on(qEmployee.department.id.eq(qDepartment.id))

                .leftJoin(qEmployee.ranks, qRanks)
                .on(qEmployee.ranks.id.eq(qRanks.id))

                .leftJoin(qEmployee.affiliation, qAffiliation)
                .on(qEmployee.affiliation.id.eq(qAffiliation.id))

                .where(containsName(name),
                        eqAffiliation(affiliation),
                        eqDepartment(department)
                )

                .orderBy(humanResourceSort(pageable))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch()
                ;

        return new PageImpl<>(hrDepartmentInfoList, pageable, getTeamTotalPage(name, affiliation, department));
    }

    private Long getTeamTotalPage(String name, String affiliation, String department) {
        return jpaQueryFactory.select(qEmployee.count())
                .from(qEmployee)

                .leftJoin(qEmployee.department, qDepartment)
                .on(qEmployee.department.id.eq(qDepartment.id))

                .leftJoin(qEmployee.ranks, qRanks)
                .on(qEmployee.ranks.id.eq(qRanks.id))

                .leftJoin(qEmployee.affiliation, qAffiliation)
                .on(qEmployee.affiliation.id.eq(qAffiliation.id))

                .where(containsName(name),
                        eqAffiliation(affiliation),
                        eqDepartment(department)
                )

                .fetchOne()
                ;

    }

    private BooleanExpression containsName(String name) {
        if (name == null) return null;

        return qEmployee.nameKr.contains(name);
    }

    private BooleanExpression eqAffiliation(String affiliation) {
        if (affiliation == null) return null;

        return qAffiliation.name.stringValue().eq(affiliation);
    }

    private BooleanExpression eqDepartment(String department) {
        if (department == null) return null;

        return qDepartment.name.stringValue().eq(department);
    }



}
