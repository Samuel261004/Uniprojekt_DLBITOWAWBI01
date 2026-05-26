package com.example.demo.repository;

import com.example.demo.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestRepository
        extends JpaRepository<ServiceRequest, Long> {
}