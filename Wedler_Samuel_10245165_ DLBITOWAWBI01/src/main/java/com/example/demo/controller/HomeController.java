package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.ServiceOfferRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class HomeController {


    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ServiceOfferRepository repository;

    public HomeController(ServiceOfferRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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

    @GetMapping("/registerSuccess")
    public String registerSuccess() {
        return "registerSuccess";
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

        @GetMapping("/register")
    public String showRegistrationForm() {
        System.out.println("Register");
        return "register";
    }

    @Transactional
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               @RequestParam String email,
                               Model model, HttpSession session) {
        User user;
        Role role = Role.USER;

        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordError", "Die Passwörter stimmen nicht überein.");
            return "register";
        }
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("usernameError", "Benutzername ist bereits vergeben.");
            return "register";
        }
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("emailRegisterError", "E-Mail ist bereits registriert.");
            return "register";
        }
        if ( username.length()>255) {
            model.addAttribute("usernameLengthError", "Benutzername ist zu lang");
            return "register";
        }
        if ( password.length()>255) {
            model.addAttribute("passwordLengthError", "Passwort ist zu lang");
            return "register";
        }
        if ( email.length()>255) {
            model.addAttribute("emailLengthError", "E-Mail ist zu lang");
            return "register";
        }
        else if (password.equals(confirmPassword)) {
            user = new User(username, passwordEncoder.encode(password), email, role);
            userRepository.save(user);
            session.setAttribute("user", user);
            return "redirect:/registerSuccess";
        }
        return "register";
    }
}


