package com.gangnam.portal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_no")
    private Long employeeNo;

    // 소속 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "affiliation_id", nullable = false)
    private Affiliation affiliation;

    // 부서 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // 직급 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranks_id", nullable = false)
    private Ranks ranks;

    // 권한 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

    @Column(name = "name_kr", nullable = false, columnDefinition = "VARCHAR(20)")
    private String nameKr;

    @Column(name = "name_en", nullable = false, columnDefinition = "VARCHAR(50)")
    private String nameEn;

    @Column(name = "gender", nullable = false, columnDefinition = "TINYINT(1)")
    private Integer gender;

    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(35)")
    private String address;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(15)")
    private String phone;

    @Column(name = "profile_img", columnDefinition = "VARCHAR(250)")
    private String profileImg;

    @Column(name = "state", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean state;      // 0 : 재직, 1 : 퇴직

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date birthday;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "join_date", nullable = false, columnDefinition = "DATETIME")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date joinDate;

    @OneToMany(mappedBy = "employee")
    private List<Commute> commuteList;

    @OneToMany(mappedBy = "employee")
    private List<EmployeeEmail> employeeEmailList;

}
