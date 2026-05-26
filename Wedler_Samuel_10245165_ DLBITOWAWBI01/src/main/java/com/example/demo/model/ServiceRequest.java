package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ServiceRequest {

    @Id
    @GeneratedValue
    private Long id;

    private String subject;

    @Column(length = 2000)
    private String message;

    private String status;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private ServiceOffer serviceOffer;

    public ServiceRequest() {
    }

    public ServiceRequest(String subject, String message, String status, User createdBy, ServiceOffer serviceOffer) {
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.createdBy = createdBy;
        this.serviceOffer = serviceOffer;
    }

    public Long getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public ServiceOffer getServiceOffer() {
        return serviceOffer;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setServiceOffer(ServiceOffer serviceOffer) {
        this.serviceOffer = serviceOffer;
    }
}