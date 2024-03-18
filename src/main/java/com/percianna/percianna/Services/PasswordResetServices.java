package com.percianna.percianna.Services;

import com.percianna.percianna.Dto.MailRequest;
import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.percianna.percianna.Config.CodeGenerator.generateRandomCode;

@Service
public class PasswordResetServices {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Generate a reset token (you can use UUID.randomUUID().toString())
            String resetToken = generateRandomCode();

            // Set the reset token and expiration timestamp in the user's entity
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(2)); // Adjust expiration time

            // Save the user entity
            userRepository.save(user);

            // Send an email with the reset link
            sendPasswordResetEmail(user.getEmail(), resetToken);
        }
    }

    private void sendPasswordResetEmail(String email, String resetToken) {
        // Prepare the email model with the reset token
        Map<String, Object> model = new HashMap<>();
        model.put("resetToken", resetToken);

        // Create the MailRequest object and call the email service
        MailRequest mailRequest = new MailRequest();
        User user = new User();

        user.setEmail(email);
        mailRequest.setTo(email);
        //mailRequest.setSubject("Password Reset Request");
        mailRequest.setFrom("louisfranck.moussima@esprit.tn"); // Set your email here
        emailService.sendEmail(mailRequest, model);
    }

    // Rest of the methods
}


