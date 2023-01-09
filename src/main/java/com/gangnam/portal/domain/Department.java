package com.gangnam.portal.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private DepartmentName name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "department")
    private List<Team> teamList;
}
