package com.percianna.percianna.Config;

import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
@Configuration
public class CodeGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789*-+/*$£ù!§"; // Define the characters you want to include in the code.
    private static final int CODE_LENGTH = 10; // Define the desired code length.

    public static String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }

    public static void main(String[] args) {
        String randomCode = generateRandomCode();
        System.out.println("Generated Code: " + randomCode);
    }
}