package com.gangnam.portal.util.jwt.customUserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gangnam.portal.domain.EmployeeEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails {

    private String username;
    private List<String> roles;

    public static CustomUserDetails of(EmployeeEmail employeeEmail) {
        List<String> roles = new ArrayList<>();
        roles.add(employeeEmail.getEmployee().getAuthority().getName().toString());
        System.out.println(employeeEmail.getEmployee().getAuthority().getName().toString());

        return CustomUserDetails.builder()
                .username(employeeEmail.getEmail())
                .roles(roles)
                .build();
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return false;
    }
}
