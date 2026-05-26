package com.example.demo.repository;

import com.example.demo.model.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<user, Long> {

    @Query(value = "SELECT * FROM USERS u WHERE u.name = ?1", nativeQuery = true)
    user findByName(String name);
    user findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByName(String username);
    Optional<user> findById(Long id);
}
