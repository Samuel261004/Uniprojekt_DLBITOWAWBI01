package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.repository.ServiceOfferRepository;

@Controller
public class HomeController {

    private final ServiceOfferRepository repository;

    public HomeController(ServiceOfferRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute(
            "services",
            repository.findAll()
        );

        return "index";
    }

    @GetMapping("/home")
    public String homePage() {
        return "index";
    }

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    @GetMapping("/login")
    public String login() {
        System.out.println("Login");
        return "login";
    }
}