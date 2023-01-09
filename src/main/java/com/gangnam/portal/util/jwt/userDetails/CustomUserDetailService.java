package com.gangnam.portal.util.jwt.userDetails;


import com.gangnam.portal.domain.Employee;
import com.gangnam.portal.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Override
//    @Cacheable(value = CacheKey.USER, key = "#userId", unless = "#result == null")
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        Optional<Member> findMember = memberRepository.findByUserId(userId);

        Employee findMember = employeeRepository.findByEmail(userId)
                .orElseThrow(() -> new NoSuchElementException("없는 회원입니다."));

        return CustomUserDetails.of(findMember);

    }
}
