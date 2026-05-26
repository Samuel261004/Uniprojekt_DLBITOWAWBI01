package com.example.demo;

import com.example.demo.model.SecurityUser;
import com.example.demo.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("CustomAuthenticationProvider");

        if (authentication == null) {
            System.out.println("Authentication is null");
            return null;
        }
        final String username = authentication.getName();
        System.out.println("Username: " + username);
        final String password = authentication.getCredentials().toString();
        System.out.println("Password: " + password);
        SecurityUser securityUser= userDetailsService.loadUserByUsername(username);
        System.out.println("SecurityUser: " + securityUser);
        if (securityUser == null) {
            System.out.println("User not found");
            return null;
        }
        if (securityUser!=null) {
            System.out.println("User found");
        }
        if (!passwordEncoder.matches(password, securityUser.getPassword())) {
            System.out.println("Wrong password");
            return null;
        }
        return new UsernamePasswordAuthenticationToken(username, password, securityUser.getAuthorities());
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
