package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ServiceOffer;

public interface ServiceOfferRepository
        extends JpaRepository<ServiceOffer, Long> {
                boolean existsByTitle(String title);
}