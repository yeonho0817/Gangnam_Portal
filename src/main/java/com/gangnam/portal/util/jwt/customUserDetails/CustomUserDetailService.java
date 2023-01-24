package com.gangnam.portal.util.jwt.customUserDetails;

import com.gangnam.portal.domain.EmployeeEmail;
import com.gangnam.portal.repository.custom.EmployeeEmailCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final EmployeeEmailCustomRepository employeeEmailCustomRepository;

    public CustomUserDetails loadUserByUsername(String email, String provider) throws UsernameNotFoundException {
        EmployeeEmail findEmployeeEmail = employeeEmailCustomRepository.isExists(email, provider)
                .orElseThrow(NoSuchElementException::new);

        return CustomUserDetails.of(findEmployeeEmail);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
