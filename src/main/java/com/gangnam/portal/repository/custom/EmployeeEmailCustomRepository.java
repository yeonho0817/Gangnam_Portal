package com.gangnam.portal.repository.custom;

import com.gangnam.portal.domain.EmployeeEmail;

import java.util.Optional;

public interface EmployeeEmailCustomRepository {
    Optional<EmployeeEmail> isExists(String email, String provider);

}
