package com.example.demo.repository;

import com.example.demo.model.ServiceOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferRepository
        extends JpaRepository<ServiceOffer, Long> {
}