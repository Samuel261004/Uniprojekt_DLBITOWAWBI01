package com.example.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.UserSettingsDto;
import com.example.demo.model.Notification;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
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
    private final NotificationRepository notificationRepository;


    public HomeController(ServiceOfferRepository repository, PasswordEncoder passwordEncoder, NotificationRepository notificationRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/")
    public String home(Model model) {

         Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);

        Long userId = user.getId();

        List<Notification> unreadNotifications =
                notificationRepository.findByUserIdAndReadFalse(userId);

        model.addAttribute("services", repository.findAll());
        model.addAttribute("notifications", unreadNotifications);
        model.addAttribute("hasUnread", !unreadNotifications.isEmpty());

        return "index";
    }

    @PostMapping("/notifications/read")
    public String markNotificationsAsRead() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        Long userId = user.getId();

        List<Notification> notifications =
                notificationRepository.findByUserIdAndReadFalse(userId);

        notifications.forEach(notification -> notification.setRead(true));

        notificationRepository.saveAll(notifications);

        return "redirect:/";
    }

    @GetMapping("/home")
    public String homePage() {
        return "index";
    }

    @GetMapping("/registerSuccess")
    public String registerSuccess() {
        return "registerSuccess";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @PostMapping("/settings")
    @ResponseBody
    public void saveSettings(@RequestBody UserSettingsDto dto,
                         Authentication authentication) {

        User user = userRepository.findByUsername(authentication.getName());

        user.setFontSize(dto.getFontSize());
        user.setHighContrast(dto.isHighContrast());

        userRepository.save(user);
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
        if ( username.length()>255) {
            model.addAttribute("usernameLengthError", "Benutzername ist zu lang");
            return "register";
        }
        if ( password.length()>255) {
            model.addAttribute("passwordLengthError", "Passwort ist zu lang");
            return "register";
        }
        else if (password.equals(confirmPassword)) {
            user = new User(username, passwordEncoder.encode(password), role, "medium", false);
            userRepository.save(user);
            session.setAttribute("user", user);
            return "redirect:/registerSuccess";
        }
        return "register";
    }


}


