package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Notification;
import com.example.demo.model.Role;
import com.example.demo.model.ServiceOffer;
import com.example.demo.model.ServiceRequest;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.ServiceOfferRepository;
import com.example.demo.repository.ServiceRequestRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class AdminController {
    
    @Autowired
    private UserRepository userRepository;

    private final ServiceOfferRepository offerRepository;

    private final ServiceRequestRepository repository;

    private final NotificationRepository notificationRepository;

    public AdminController(
            ServiceRequestRepository repository,
            ServiceOfferRepository offerRepository,
            NotificationRepository notificationRepository) {

        this.repository = repository;
        this.offerRepository = offerRepository;
        this.notificationRepository = notificationRepository;
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

    @GetMapping("/admin/role-management")
        public String roleManagement() {
        return "admin-rolemanagement";
    }

    @PostMapping("/admin/role-management")
    public String updateRole(
            @RequestParam Long userId,
            @RequestParam Role role) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        user.setRole(role);

        userRepository.save(user);

        return "redirect:/admin/role-management";
    }
    @PostMapping("/admin/request/{id}/accept")
    public String acceptRequest(@PathVariable Long id) {

        ServiceRequest request = repository.findById(id)
            .orElseThrow();

        request.setStatus("ACCEPTED");

        repository.save(request);

        Notification notification = new Notification();

        notification.setUserId(
                request.getCreatedBy().getId());

        notification.setText(
                "Ihre Anfrage '" +
                request.getSubject() +
                "' wurde angenommen.");

        notification.setRead(false);

        notificationRepository.save(notification);

        return "redirect:/admin/requests";
    }

    @PostMapping("/admin/request/{id}/reject")
    public String rejectRequest(@PathVariable Long id) {

        ServiceRequest request = repository.findById(id)
                .orElseThrow();

        request.setStatus("REJECTED");

        repository.save(request);

        Notification notification = new Notification();

        notification.setUserId(
                request.getCreatedBy().getId());

        notification.setText(
                "Ihre Anfrage '" +
                request.getSubject() +
                "' wurde abgelehnt.");

        notification.setRead(false);

        notificationRepository.save(notification);

        return "redirect:/admin/requests";
    }
}