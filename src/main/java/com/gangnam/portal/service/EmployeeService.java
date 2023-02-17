package com.gangnam.portal.service;


import com.gangnam.portal.domain.*;
import com.gangnam.portal.dto.AuthenticationDTO;
import com.gangnam.portal.dto.EmployeeDTO;
import com.gangnam.portal.dto.Response.ErrorStatus;
import com.gangnam.portal.dto.Response.ResponseData;
import com.gangnam.portal.dto.Response.Status;
import com.gangnam.portal.exception.CustomException;
import com.gangnam.portal.repository.AuthorityRepository;
import com.gangnam.portal.repository.EmployeeEmailRepository;
import com.gangnam.portal.repository.EmployeeRepository;
import com.gangnam.portal.repository.RanksRepository;
import com.gangnam.portal.repository.custom.EmployeeCustomRepository;
import com.gangnam.portal.repository.custom.TeamCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeCustomRepository employeeCustomRepository;
    private final TeamCustomRepository teamCustomRepository;
    private final AuthorityRepository authorityRepository;
    private final RanksRepository ranksRepository;
    private final EmployeeEmailRepository employeeEmailRepository;

    @Value("${profilePath}")
    private String profilePath;

    @Value("${companyEmail}")
    private String companyEmail;

    // 사원 추가
    @Modifying
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData saveEmployee(EmployeeDTO.EmployeeAdminInfo employeeSaveInfo) {
        checkDuplicated("save", employeeSaveInfo);

        // affiliation + department
        Department findDepartment = teamCustomRepository.findByDepartmentId(employeeSaveInfo.getAffiliationId())
                .orElseThrow(() -> new CustomException(ErrorStatus.FIND_DEPARTMENT_FAILED));

        // ranks
        Ranks findRanks = ranksRepository.findById(employeeSaveInfo.getRankId())
                .orElseThrow(() -> new CustomException(ErrorStatus.FIND_RANK_FAILED));

        // authority
        Authority findAuthority = authorityRepository.findById(employeeSaveInfo.getRoleId())
                .orElseThrow(() -> new CustomException(ErrorStatus.FIND_AUTHORITY_FAILED));

        // email
//        List<EmployeeEmail> newEmployeeEmails = new ArrayList<>();
//        newEmployeeEmails.add(EmployeeEmail.builder()
//                    .email(employeeSaveInfo.getGoogleEmail() + "@twolinecode.com")
//                    .provider(Provider.google)
//                .build());
//        newEmployeeEmails.add(EmployeeEmail.builder()
//                    .email(employeeSaveInfo.getKakaoEmail())
//                    .provider(Provider.kakao)
//                .build());

        // 사진 저장
//        saveProfileImage(employeeSaveInfo.getProfileImg(), employeeSaveInfo.getNameKr(), employeeSaveInfo.getGoogleEmail());



        Employee newEmployee = Employee.builder()
                .employeeNo(employeeSaveInfo.getEmployeeNo())
                .affiliation(findDepartment.getAffiliation())
                .department(findDepartment)
                .ranks(findRanks)
                .authority(findAuthority)
                .nameKr(employeeSaveInfo.getNameKr())
                .nameEn(employeeSaveInfo.getNameEn())
                .gender(employeeSaveInfo.getGender().equals("남자") ? 1 : 2)
                .address(employeeSaveInfo.getAddress())
                .phone(employeeSaveInfo.getPhone())
                .state(!employeeSaveInfo.getState().equals("재직"))
                .birthday(employeeSaveInfo.getBirthday())
                .joinDate(employeeSaveInfo.getJoinDate())

//                .profileImg(profilePath + "\\" + employeeSaveInfo.getNameKr() + "-" + employeeSaveInfo.getGoogleEmail() + companyEmail)

                .build();

        EmployeeEmail googleEmail = EmployeeEmail.builder()
                    .email(employeeSaveInfo.getGoogleEmail() + companyEmail)
                    .employee(newEmployee)
                    .provider(Provider.google)
                .build();

        newEmployee.addEmployeeEmail(googleEmail);

        if (StringUtils.hasText(employeeSaveInfo.getKakaoEmail())) {
            EmployeeEmail kakaoEmail = EmployeeEmail.builder()
                    .email(employeeSaveInfo.getKakaoEmail())
                    .employee(newEmployee)
                    .provider(Provider.kakao)
                    .build();

            newEmployee.addEmployeeEmail(kakaoEmail);
        }

        employeeRepository.save(newEmployee);

        return new ResponseData<>(Status.SAVE_EMPLOYEE_SUCCESS, Status.SAVE_EMPLOYEE_SUCCESS.getDescription());
    }

    // 사원 수정
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData updateEmployeeInfo(EmployeeDTO.EmployeeAdminInfo employeeUpdateInfo) {
        // 중복 검사
        checkDuplicated("update", employeeUpdateInfo);

        // affiliation + department
        Department findDepartment = teamCustomRepository.findByDepartmentId(employeeUpdateInfo.getAffiliationId())
                .orElseThrow(() -> new CustomException(ErrorStatus.FIND_DEPARTMENT_FAILED));

        // ranks
        Ranks findRanks = ranksRepository.findById(employeeUpdateInfo.getRankId())
                .orElseThrow(() -> new CustomException(ErrorStatus.FIND_RANK_FAILED));

        // authority
        Authority findAuthority = authorityRepository.findById(employeeUpdateInfo.getRoleId())
                .orElseThrow(() -> new CustomException(ErrorStatus.FIND_AUTHORITY_FAILED));

        // employee
        Employee findEmployee = employeeRepository.findById(employeeUpdateInfo.getEmployeeId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        findEmployee.updateEmployeeNo(employeeUpdateInfo.getEmployeeNo());
        findEmployee.updateAffiliation(findDepartment.getAffiliation());
        findEmployee.updateDepartment(findDepartment);
        findEmployee.updateRanks(findRanks);
        findEmployee.updateAuthority(findAuthority);
        findEmployee.updateNameKr(employeeUpdateInfo.getNameKr());
        findEmployee.updateNameEn(employeeUpdateInfo.getNameEn());
        findEmployee.updateGender(employeeUpdateInfo.getGender().equals("남자") ? 1 : 0);
        findEmployee.updateAddress(employeeUpdateInfo.getAddress());
        findEmployee.updatePhone(employeeUpdateInfo.getPhone());
        findEmployee.updateState(!employeeUpdateInfo.getState().equals("재직"));
        findEmployee.updateBirthday(employeeUpdateInfo.getBirthday());
        findEmployee.updateJoinDate(employeeUpdateInfo.getJoinDate());
//        findEmployee.updateProfileImg(profilePath + "\\" + employeeUpdateInfo.getNameKr() + "-" + employeeUpdateInfo.getGoogleEmail() + companyEmail);

        // email
        List<EmployeeEmail> findEmployeeEmail = findEmployee.getEmployeeEmailList();

        if (findEmployeeEmail.size() == 1) {
            findEmployeeEmail.get(0).updateEmail(employeeUpdateInfo.getGoogleEmail() + companyEmail);

            if (! employeeUpdateInfo.getKakaoEmail().isBlank()) {
                findEmployee.addEmployeeEmail(EmployeeEmail.builder()
                            .employee(findEmployee)
                            .email(employeeUpdateInfo.getKakaoEmail())
                            .provider(Provider.kakao)
                        .build());
            }
        } else {
            findEmployeeEmail.get(0).updateEmail(employeeUpdateInfo.getGoogleEmail() + companyEmail);

            if (! employeeUpdateInfo.getKakaoEmail().isEmpty()) {
                findEmployeeEmail.get(1).updateEmail(employeeUpdateInfo.getKakaoEmail());
            } else {
                findEmployee.getEmployeeEmailList().remove(findEmployeeEmail.get(1));
            }
        }

        return new ResponseData<>(Status.UPDATE_EMPLOYEE_SUCCESS, Status.UPDATE_EMPLOYEE_SUCCESS.getDescription());
    }

    private void checkDuplicateEmail(String mode, Long employeeId, String email, Provider provider) {
        Optional<EmployeeEmail> findEmployeeEmail = employeeEmailRepository.findByEmailAndProviderWithEmployee(email , provider);
        if (mode.equals("save")) {
            if (findEmployeeEmail.isPresent())
                if (provider == Provider.google)
                    throw new CustomException(ErrorStatus.DUPLICATED_GOOGLE_EMAIL);
                else
                    throw new CustomException(ErrorStatus.DUPLICATED_KAKAO_EMAIL);
        } else {
            if (findEmployeeEmail.isPresent() && !Objects.equals(findEmployeeEmail.get().getEmployee().getId(), employeeId))
                if (provider == Provider.google)
                    throw new CustomException(ErrorStatus.DUPLICATED_GOOGLE_EMAIL);
                else
                    throw new CustomException(ErrorStatus.DUPLICATED_KAKAO_EMAIL);
        }
    }
    private void checkDuplicateEmployeeNo(String mode, Long employeeId, Long employeeNo) {
        Optional<Employee> findEmployeeByNo = employeeRepository.findByEmployeeNo(employeeNo);
        if (mode.equals("save")) {
            if (findEmployeeByNo.isPresent()) throw new CustomException(ErrorStatus.DUPLICATED_EMPLOYEE_NO);
        } else {
            if (findEmployeeByNo.isPresent() && !Objects.equals(findEmployeeByNo.get().getId(), employeeId)) throw new CustomException(ErrorStatus.DUPLICATED_EMPLOYEE_NO);
        }
    }

    private void checkDuplicatePhone(String mode, Long employeeId, String phone) {
        Optional<Employee> findEmployeeByPhone = employeeRepository.findByPhone(phone);
        if (mode.equals("save")) {
            if (findEmployeeByPhone.isPresent()) throw new CustomException(ErrorStatus.DUPLICATED_PHONE);
        } else {
            if (findEmployeeByPhone.isPresent() && !Objects.equals(findEmployeeByPhone.get().getId(), employeeId)) throw new CustomException(ErrorStatus.DUPLICATED_PHONE);
        }
    }

    private void checkDuplicated(String mode, EmployeeDTO.EmployeeAdminInfo employeeAdminInfo) {
        // 구글 이메일 검사
        checkDuplicateEmail(mode, employeeAdminInfo.getEmployeeId(), employeeAdminInfo.getGoogleEmail() + companyEmail, Provider.google);

        // 카카오 이메일 검사
        if (! employeeAdminInfo.getKakaoEmail().isEmpty()) {
            checkDuplicateEmail(mode, employeeAdminInfo.getEmployeeId(), employeeAdminInfo.getKakaoEmail(), Provider.kakao);
        }

        // 사번
        checkDuplicateEmployeeNo(mode, employeeAdminInfo.getEmployeeId(), employeeAdminInfo.getEmployeeNo());

        // 전화번호
        checkDuplicatePhone(mode, employeeAdminInfo.getEmployeeId(), employeeAdminInfo.getPhone());
    }

    private void saveProfileImage(MultipartFile multipartFile, String nameKr, String googleEmail) {
        try {
            multipartFile.transferTo(new File(profilePath + "\\" + nameKr + "-" + googleEmail + companyEmail));
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.SAVE_PROFILE_IMAGE_FAILED);
        }
    }

    // 사원 정보 조회
    @Transactional(readOnly = true)
    public ResponseData<EmployeeDTO.EmployeeInfoDTO> findEmployeeInfo(Long employeeId) {
        EmployeeDTO.EmployeeInfoDTO findEmployeeInfo = employeeCustomRepository.findEmployeeInfo(employeeId)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        return new ResponseData<>(Status.FIND_MY_INFO_SUCCESS, Status.FIND_MY_INFO_SUCCESS.getDescription(), findEmployeeInfo);
    }
    // 내 정보 조회
    @Transactional(readOnly = true)
    public ResponseData<EmployeeDTO.EmployeeInfoDTO> findMyInfo(UsernamePasswordAuthenticationToken authenticationToken) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        EmployeeDTO.EmployeeInfoDTO findEmployeeInfo = employeeCustomRepository.findEmployeeInfo(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        return new ResponseData<>(Status.FIND_MY_INFO_SUCCESS, Status.FIND_MY_INFO_SUCCESS.getDescription(), findEmployeeInfo);
    }

    // 내 정보 수정    O
    @Transactional(rollbackFor = {Exception.class})
    public ResponseData updateMyInfo(UsernamePasswordAuthenticationToken authenticationToken, EmployeeDTO.UpdateInfoDTO updateInfoDTO) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO(authenticationToken);

        Employee findEmployee = employeeRepository.findById(authenticationDTO.getId())
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMPLOYEE));

        EmployeeEmail findEmployeeEmail = employeeEmailRepository.findByEmployeeIdAndProvider(findEmployee.getId(), Provider.google)
                .orElseThrow(() -> new CustomException(ErrorStatus.NOT_FOUND_EMAIL));

        saveProfileImage(updateInfoDTO.getProfileImg(), findEmployee.getNameKr(), findEmployeeEmail.getEmail());

        findEmployee.updateNameEn(updateInfoDTO.getNameEn());
        findEmployee.updatePhone(updateInfoDTO.getPhone());
        findEmployee.updateAddress(updateInfoDTO.getAddress());
        findEmployee.updateProfileImg(profilePath + "\\" + findEmployee.getNameKr() + "-" + findEmployeeEmail.getEmail() + ".jpg");

        return new ResponseData(Status.UPDATE_MY_INFO_SUCCESS, Status.UPDATE_MY_INFO_SUCCESS.getDescription());
    }

    // 출퇴근 수정 -> 직원 목록
    @Transactional(readOnly = true)
    public ResponseData<List<EmployeeDTO.EmployeeNameList>> readEmployeeNameList() {
        List<EmployeeDTO.EmployeeNameList> employeeNameList = employeeCustomRepository.readEmployeeNameList().stream()
                .peek(employee -> employee.setName(employee.getName() + " (사번 : " + employee.getEmployeeNo() + ")"))
                .collect(Collectors.toList());

        return new ResponseData<>(Status.READ_SUCCESS, Status.READ_SUCCESS.getDescription(), employeeNameList);
    }

}
