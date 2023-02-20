package com.gangnam.portal.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
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

    @Column(name = "address", nullable = false, columnDefinition = "VARCHAR(200)")
    private String address;

    @Column(name = "phone", nullable = false, columnDefinition = "VARCHAR(15)")
    private String phone;

    @Column(name = "profile_img", columnDefinition = "VARCHAR(500)")
    private String profileImg;

    @Column(name = "state", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean state;      // 0 : 재직, 1 : 퇴직

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date birthday;

    @Temporal(TemporalType.DATE)
    @Column(name = "join_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date joinDate;

    @OneToMany(mappedBy = "employee")
    private List<Commute> commuteList;

    @BatchSize(size = 2)
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EmployeeEmail> employeeEmailList = new ArrayList<>();

    public void addEmployeeEmail(EmployeeEmail employeeEmail) {
        this.employeeEmailList.add(employeeEmail);
    }

    public void updateNameEn(String nameEn){
        if (nameEn == null) return;
        this.nameEn = nameEn;
    }

    public void updatePhone(String phone){
        if (phone == null) return;
        this.phone = phone;
    }

    public void updateAddress(String address){
        if (address == null) return;
        this.address = address;
    }

    public void updateEmployeeNo(Long employeeNo) {
        if (employeeNo == null) return;
        this.employeeNo = employeeNo;
    }

    public void updateAffiliation(Affiliation affiliation) {
        if (affiliation == null) return;
        this.affiliation = affiliation;
    }

    public void updateDepartment(Department department) {
        if (department == null) return;
        this.department = department;
    }

    public void updateRanks(Ranks ranks) {
        if (ranks == null) return;
        this.ranks = ranks;
    }

    public void updateAuthority(Authority authority) {
        if (authority == null) return;
        this.authority = authority;
    }

    public void updateNameKr(String nameKr) {
        if (nameKr == null) return;
        this.nameKr = nameKr;
    }

    public void updateGender(Integer gender) {
        if (gender == null) return;
        this.gender = gender;
    }

    public void updateProfileImg(String profileImg) {
//        if (profileImg == null) return;
        this.profileImg = profileImg;
    }

    public void updateState(Boolean state) {
        if (state == null) return;
        this.state = state;
    }

    public void updateBirthday(Date birthday) {
        if (birthday == null) return;
        this.birthday = birthday;
    }

    public void updateJoinDate(Date joinDate) {
        if (joinDate == null) return;
        this.joinDate = joinDate;
    }
}
