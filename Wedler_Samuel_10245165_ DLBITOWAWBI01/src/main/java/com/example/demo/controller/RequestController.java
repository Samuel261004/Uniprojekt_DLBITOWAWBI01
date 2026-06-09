package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.ServiceRequestDto;
import com.example.demo.model.ServiceOffer;
import com.example.demo.model.ServiceRequest;
import com.example.demo.model.User;
import com.example.demo.repository.ServiceOfferRepository;
import com.example.demo.repository.ServiceRequestRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class RequestController {

    private final ServiceRequestRepository repository;

    @Autowired
    private ServiceOfferRepository serviceOfferRepository;

    @Autowired
    private UserRepository userRepository;

    public RequestController(ServiceRequestRepository repository) {
        this.repository = repository;
    }

@GetMapping("/request")
public String form(Model model) {

    model.addAttribute("services", serviceOfferRepository.findAll());

    return "request-form";
}

@PostMapping("/request")
public String submit(ServiceRequestDto dto) {

    ServiceRequest request = new ServiceRequest();

    request.setSubject(dto.getSubject());
    request.setMessage(dto.getMessage());
    request.setStatus("OFFEN");

    
    ServiceOffer service = serviceOfferRepository.findById(dto.getServiceId())
            .orElseThrow();

    request.setServiceOffer(service);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    User user = userRepository.findByUsername(auth.getName());

    request.setCreatedBy(user);

    repository.save(request);

    return "confirmation";
}
}