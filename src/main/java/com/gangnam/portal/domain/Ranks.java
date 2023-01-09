package com.gangnam.portal.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ranks")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ranks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private RankName name;

    @OneToMany(mappedBy = "ranks")
    private List<Employee> employeeList;
}
