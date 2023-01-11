package com.gangnam.portal.dto.Response;

import com.gangnam.portal.util.jwt.customUserDetails.CustomUserDetails;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Data
public class AuthenticationDTO {
    private Long id;
    private String email;
    private String provider;
    private String role;

    public AuthenticationDTO(UsernamePasswordAuthenticationToken authenticationToken) {
        this.id = ((CustomUserDetails) authenticationToken.getPrincipal()).getId();
        this.email = ((CustomUserDetails) authenticationToken.getPrincipal()).getUsername();
        this.provider = ((CustomUserDetails) authenticationToken.getPrincipal()).getProvider();

        this.role = authenticationToken.getAuthorities().toString().substring(6, authenticationToken.getAuthorities().toString().length()-1);
    }



}
