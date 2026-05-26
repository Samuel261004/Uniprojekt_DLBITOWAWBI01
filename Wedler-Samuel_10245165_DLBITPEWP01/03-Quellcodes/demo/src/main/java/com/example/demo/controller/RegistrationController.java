package com.example.demo.controller;

import com.example.demo.VerificationCodeGenerator;
import com.example.demo.model.user;
import com.example.demo.model.UserVerification;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserVerificationRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;


@Controller
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserVerificationRepository verificationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


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
        user user;
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordError", "Die Passwörter stimmen nicht überein.");
            return "register";
        }
        if (userRepository.existsByName(username)) {
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
            user = new user(username, passwordEncoder.encode(password), email);
            user.setDate(LocalDate.now());
            userRepository.save(user);
            System.out.println("Registered unverified user");
            verificationRepository.deleteAllByEmail(email);
        
            String code = VerificationCodeGenerator.generateVerificationCode();
            UserVerification verification = new UserVerification();
            verification.setEmail(email);
            verification.setVerificationCode(code);
            verificationRepository.save(verification);
            System.out.println("saved verification");
        	System.out.println(code);
        
        	try {
        		emailService.sendVerificationEmail(email, code);
        		System.out.println("Email versendet");
        	} catch (Exception e) {
        		System.err.println("Fehler beim Senden der E-Mail: " + e.getMessage());
        		model.addAttribute("emailError", "Die Verifizierungs-E-Mail konnte nicht gesendet werden.");
        	}
        }
        	model.addAttribute("email", email);
        	session.setAttribute("email", email);
        	return "verify";
        
    }


    @GetMapping("/verify")
    public String showVerificationForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "verify";
    }


    @PostMapping("/verify")
    public String verifyCode(@RequestParam String email,
                             @RequestParam String code,
                             Model model) {
        boolean success = verificationService.verifyCode(email, code);

        if (success) {
        	System.out.println("Nutzer verifizieren");
            user user = userRepository.findByEmail(email);
            if (user != null) {
                user.setVerified(true);
                userRepository.save(user);
            }
            return "redirect:/verificationSuccess";
        } else {
            model.addAttribute("error", "Falscher Code. Bitte erneut versuchen.");
            model.addAttribute("email", email);
            return "verify";
        }
    }
    @Transactional
    @PostMapping("/resendVerificationCode")
    public String resendVerificationCode(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if (email == null) {
            model.addAttribute("error", "Keine gültige E-Mail gefunden.");
            return "verify";
        }
        user existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            model.addAttribute("error", "Keine Benutzer mit dieser E-Mail gefunden.");
            return "verify";
        }

        String newCode = VerificationCodeGenerator.generateVerificationCode();
        verificationRepository.deleteAllByEmail(email);

        UserVerification verification = new UserVerification();
        verification.setEmail(email);
        verification.setVerificationCode(newCode);
        verificationRepository.save(verification);


        try {
            emailService.sendVerificationEmail(email, newCode);
            model.addAttribute("success", "Der Verifizierungscode wurde erneut gesendet.");
        } catch (Exception e) {
            model.addAttribute("error", "Fehler beim Senden der E-Mail. Bitte versuche es später erneut.");
        }

        model.addAttribute("email", email);
        return "verify";
    }

    @GetMapping("/verificationSuccess")
    public String verificationSuccess() {
        return "verificationSuccess";
    }
}
