package com.example.demo.service;

import com.example.demo.model.SecurityUser;
import com.example.demo.model.user;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JpaUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        user user= userRepository.findByName(username);
        if (user == null) {
                System.out.println("user not found");
                throw new UsernameNotFoundException("user not found");
        }
        return new SecurityUser(user);
    }
}
