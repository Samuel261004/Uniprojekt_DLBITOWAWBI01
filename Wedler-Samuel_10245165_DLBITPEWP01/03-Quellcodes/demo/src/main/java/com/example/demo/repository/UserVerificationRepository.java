package com.example.demo.repository;

import com.example.demo.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    UserVerification findByEmail(String email);
    void deleteAllByEmail(String email);
}
