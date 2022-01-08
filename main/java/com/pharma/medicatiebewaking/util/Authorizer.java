package com.pharma.medicatiebewaking.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Authorizer {
    private static final Random RANDOM = new SecureRandom();

    // Generate random salt for hashing
    public String GenerateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }

    // Validate hash with login hash
    public Boolean ValidatePassword(String databaseHash, String inputHash) {
        boolean result = false;

        if (databaseHash.isEmpty() || inputHash.isEmpty()) {
            result = false;
        }

        if (databaseHash.equals(inputHash)) {
            result = true;
        }
        return result;
    }

    // Hash password with salt
    public String HashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String test = password + salt;
        byte[] encodedHash = digest.digest(test.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encodedHash);
    }
}
