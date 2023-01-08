package com.gangnam.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "affiliation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Affiliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private AffiliationName name;

    @OneToMany(mappedBy = "affiliation")
    private List<Employee> employeeList;

    @OneToMany(mappedBy = "affiliation")
    private List<Department> departmentList;
}
