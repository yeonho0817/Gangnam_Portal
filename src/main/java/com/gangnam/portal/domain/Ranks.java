package com.gangnam.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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


    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(50)")
    private RankName name;

    @OneToMany(mappedBy = "ranks")
    private List<Employee> employeeList;
}
