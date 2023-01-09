package com.gangnam.portal.repository.custom;

import com.gangnam.portal.dto.EmployeeDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeCustomRepositoryImpl implements EmployeeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<EmployeeDTO.ReadHumanResource> readHumanResource(String sort, String orderBy, String selectValue, String searchTxt, Integer pageSize, Integer pageNumber) {


        return null;
    }
}
