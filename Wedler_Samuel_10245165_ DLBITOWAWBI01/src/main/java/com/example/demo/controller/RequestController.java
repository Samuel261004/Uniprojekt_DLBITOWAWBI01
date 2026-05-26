package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.ServiceRequest;
import com.example.demo.repository.ServiceRequestRepository;

@Controller
public class RequestController {

    private final ServiceRequestRepository repository;

    public RequestController(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/request")
    public String form() {
        return "request-form";
    }

    @PostMapping("/request")
    public String submit(ServiceRequest request) {

        request.setStatus("OFFEN");

        repository.save(request);

        return "confirmation";
    }
}