package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.ServiceOffer;
import com.example.demo.repository.ServiceOfferRepository;
import com.example.demo.repository.ServiceRequestRepository;

@Controller
public class AdminController {

    private final ServiceOfferRepository offerRepository;

    private final ServiceRequestRepository repository;

    public AdminController(ServiceRequestRepository repository, ServiceOfferRepository offerRepository) {
        this.repository = repository;
        this.offerRepository = offerRepository;
    }

    @GetMapping("/admin/requests")
    public String requests(Model model) {

        model.addAttribute(
            "requests",
            repository.findAll()
        );

        return "admin-requests";
    }

    @GetMapping("/admin/offers")
    public String offers(Model model) {

        model.addAttribute(
            "services",
            offerRepository.findAll()
        );

        return "admin-offers";
    }

    @PostMapping("/admin/offer")
    public String addOffer(@RequestParam String title, @RequestParam String description) {

        offerRepository.save(new ServiceOffer(title, description));

        return "redirect:/admin/offers";
    }

    @GetMapping("/admin/edit/{id}")
    public String editServicePage(@PathVariable Long id, Model model) {

        ServiceOffer service = offerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service nicht gefunden"));

        model.addAttribute("service", service);

        return "edit-service";
    }   

    @PostMapping("/admin/edit/{id}")
    public String updateService(
        @PathVariable Long id,
        @RequestParam String title,
        @RequestParam String description) {

        ServiceOffer service = offerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service nicht gefunden"));

        service.setTitle(title);
        service.setDescription(description);

        offerRepository.save(service);

        return "redirect:/admin";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteService(@PathVariable Long id) {

        offerRepository.deleteById(id);

        return "redirect:/admin";
    }
}