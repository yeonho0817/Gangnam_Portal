package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.EmployeeDTO;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
    private final QAuthority qAuthority = QAuthority.authority;
    private final QEmployeeEmail qEmployeeEmail = QEmployeeEmail.employeeEmail;
    private final QDepartment qDepartment = QDepartment.department;
    private final QRanks qRanks = QRanks.ranks;
    private final QAffiliation qAffiliation = QAffiliation.affiliation;

    @Override
    public Optional<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(Long id) {

        Employee employee = jpaQueryFactory.selectFrom(qEmployee)

                .join(qEmployee.authority, qAuthority).fetchJoin()
                .join(qEmployee.ranks, qRanks).fetchJoin()
                .leftJoin(qEmployee.employeeEmailList, qEmployeeEmail).fetchJoin()
                .join(qEmployee.affiliation, qAffiliation).fetchJoin()
                .join(qEmployee.department, qDepartment).fetchJoin()

                .where(qEmployee.id.eq(id))

                .fetchOne();

        EmployeeDTO.EmployeeInfoDTO employeeInfoDTO = EmployeeDTO.EmployeeInfoDTO.builder()
                    .employeeId(employee.getId())
                    .employeeNo(employee.getEmployeeNo())
                    .roleId(employee.getAuthority().getId())
                    .role(employee.getAuthority().getName().toString().substring(5))
                    .nameKr(employee.getNameKr())
                    .nameEn(employee.getNameEn())
                    .birthday(employee.getBirthday().toString())
                    .rankId(employee.getRanks().getId())
                    .rank(employee.getRanks().getName().name())
                    .affiliationId(employee.getAffiliation().getId())
                    .affiliation(employee.getAffiliation().getName().name())
                    .departmentId(employee.getDepartment().getId())
                    .department(employee.getDepartment().getName().getName())
                    .profileImg(employee.getProfileImg())
                    .gender(employee.getGender() % 2 == 0 ? "여자" : "남자")
                    .phone(employee.getPhone())
                    .address(employee.getAddress())
                    .joinDate(employee.getJoinDate().toString())
                    .state(employee.getState() ? "퇴직" : "재직")
                    .email(employee.getEmployeeEmailList().stream()
                            .map(EmployeeEmail::getEmail)
                            .collect(Collectors.joining(",")))

                .build();

        return Optional.ofNullable(employeeInfoDTO);
    }

    @Override
    public List<EmployeeDTO.EmployeeNameList> readEmployeeNameList() {
        return jpaQueryFactory.select(Projections.fields(EmployeeDTO.EmployeeNameList.class,
                    qEmployee.id.as("employeeId"),
                    qEmployee.employeeNo.as("employeeNo"),
                    qEmployee.nameKr.as("name")
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

        List<Employee> hrInfoList = jpaQueryFactory.selectFrom(qEmployee)

                .leftJoin(qEmployee.ranks, qRanks).fetchJoin()
                .leftJoin(qEmployee.employeeEmailList, qEmployeeEmail).fetchJoin()
                .leftJoin(qEmployee.affiliation, qAffiliation).fetchJoin()
                .leftJoin(qEmployee.department, qDepartment).fetchJoin()

                .where(containsSearchText(selectValue, searchText),
                        qEmployee.state.eq(false))

                .orderBy(humanResourceSort(pageable))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        List<EmployeeDTO.HRInfoData> hrInfoDataList = hrInfoList.stream()
                .map(employee -> EmployeeDTO.HRInfoData.builder()
                        .employeeId(employee.getId())
                        .nameKr(employee.getNameKr())
                        .rank(employee.getRanks().getName().name())
                        .affiliation(employee.getAffiliation().getName().name())
                        .department(employee.getDepartment().getName().getName())
                        .phone(employee.getPhone())
                        .email(employee.getEmployeeEmailList().stream()
                                .map(EmployeeEmail::getEmail)
                                .collect(Collectors.toList()))

                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(hrInfoDataList, pageable, getHRTotalPage(selectValue, searchText));
    }

    @Override
    public Page<EmployeeDTO.HRInfoData> test(Pageable pageable, String selectValue, String searchText) {
        List<EmployeeEmail> hrInfoList = jpaQueryFactory.selectFrom(qEmployeeEmail)
                .rightJoin(qEmployeeEmail.employee, qEmployee).fetchJoin()
                .leftJoin(qEmployee.ranks, qRanks).fetchJoin()
                .leftJoin(qEmployee.affiliation, qAffiliation).fetchJoin()
                .leftJoin(qEmployee.department, qDepartment).fetchJoin()

                .where(containsSearchText(selectValue, searchText),
                        qEmployee.state.eq(false))

                .orderBy(humanResourceSort(pageable))

                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())

                .fetch();

        List<EmployeeDTO.HRInfoData> hrInfoDataList = hrInfoList.stream()
                .map(employeeEmail ->  EmployeeDTO.HRInfoData.builder()
                        .employeeId(employeeEmail.getEmployee().getId())
                        .nameKr(employeeEmail.getEmployee().getNameKr())
                        .rank(employeeEmail.getEmployee().getRanks().getName().name())
                        .affiliation(employeeEmail.getEmployee().getAffiliation().getName().name())
                        .department(employeeEmail.getEmployee().getDepartment().getName().getName())
                        .phone(employeeEmail.getEmployee().getPhone())
                        .email(employeeEmail.getEmployee().getEmployeeEmailList().stream()
                                .map(EmployeeEmail::getEmail)
                                .collect(Collectors.toList()))

                        .build())
                .collect(Collectors.toList());

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
                    case "namekr":
                        return new OrderSpecifier(direction, qEmployee.nameKr);
                    case "rank":
                        return new OrderSpecifier(direction, qEmployee.ranks.id);
                    case "affiliation":
                        return new OrderSpecifier(direction, qEmployee.affiliation.name);
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
    public List<EmployeeDTO.EmployeeSimpleInfo> findByEmployeeIdOrderByIdAsc(Long employeeId) {
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

                .where(eqEmployeeId(employeeId),
                        qEmployee.state.eq(false)
                )

//                .orderBy(new OrderSpecifier(Order.ASC, qEmployee.id))
                .orderBy(qEmployee.id.asc())

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

    private BooleanExpression eqEmployeeId(Long employeeId) {
        if (employeeId == null) return null;

        return qEmployee.id.eq(employeeId);
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
