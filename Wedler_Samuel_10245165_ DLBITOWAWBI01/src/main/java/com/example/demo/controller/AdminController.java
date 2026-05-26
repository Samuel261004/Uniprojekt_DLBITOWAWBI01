package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repository.ServiceRequestRepository;

@Controller
public class AdminController {

    private final ServiceRequestRepository repository;

    public AdminController(ServiceRequestRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/admin/requests")
    public String requests(Model model) {

        model.addAttribute(
            "requests",
            repository.findAll()
        );

        return "admin-requests";
    }
}