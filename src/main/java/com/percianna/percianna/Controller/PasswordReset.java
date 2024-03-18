package com.percianna.percianna.Controller;

import com.percianna.percianna.Entity.User;
import com.percianna.percianna.Repository.UserRepository;
import com.percianna.percianna.Services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
public class PasswordReset {
    @Autowired
    private UserRepository userRepository; // Your user repository
    @Autowired
    private EmailService emailService; // Your email service

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        User user = userRepository.findByEmail(email);

        if (user == null) {
            // Handle the case when the user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Generate a unique token (e.g., a random string)
        String resetToken = generateResetToken();

        // Save the token and its expiration time in the database
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Set an expiry time
        userRepository.save(user);

        // Send a password reset email
        emailService. sendPasswordResetEmail(user.getEmail(), resetToken);

        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("email");
        String token = requestData.get("token");
        String newPassword = requestData.get("newPassword");

        User user = userRepository.findByEmail(email);

        if (user == null || !isValidResetToken(user, token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token");
        }

        // Hash the new password
        String hashedPassword = passwordEncoder.encode(newPassword);

        // Update the user's password
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Clear the reset token and token expiry
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successfully");
    }

    private String generateResetToken() {
        // Generate a unique token, e.g., using UUID.randomUUID() or a random string generator
        return UUID.randomUUID().toString();
    }

    private boolean isValidResetToken(User user, String token) {
        return user.getResetToken() != null
                && user.getResetToken().equals(token)
                && user.getResetTokenExpiry() != null
                && user.getResetTokenExpiry().isAfter(LocalDateTime.now());
    }
}


