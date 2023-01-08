package com.gangnam.portal.util.jwt.customUserDetails;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        EmployeeEmail findEmployeeEmail = employeeEmailCustomRepository.isExists(email)
                .orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));

        return CustomUserDetails.of(findEmployeeEmail);
    }
}
