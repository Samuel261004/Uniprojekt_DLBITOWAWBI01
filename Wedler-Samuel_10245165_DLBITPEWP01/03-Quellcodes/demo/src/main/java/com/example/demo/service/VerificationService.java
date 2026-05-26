package com.example.demo.service;
import com.example.demo.model.UserVerification;
import com.example.demo.repository.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationService {

    @Autowired
    private UserVerificationRepository verificationRepository;

    public boolean verifyCode(String email, String code) {
        UserVerification userVerification = verificationRepository.findByEmail(email);
        if (userVerification != null && userVerification.getVerificationCode().equals(code)) {
        	System.out.println("Verifizierung erfolgreich");
            return true;
        }
        if (userVerification == null) {
        	System.out.println("Kein Nutzer gefunden " + email);
        	return false;
        }
        if (!userVerification.getVerificationCode().equals(code)) {
        	System.out.println("Falscher Code");
        	return false;
        }
        else {
        	System.out.println("Verifizierung fehlgeschlagen");
        	return false;
        }
    }
}
