package com.gangnam.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 부서 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliation_id", nullable = false)
    private Affiliation affiliation;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(100)")
    private DepartmentName departmentName;

    @OneToMany(mappedBy = "department")
    private List<Employee> employeeList;
}
