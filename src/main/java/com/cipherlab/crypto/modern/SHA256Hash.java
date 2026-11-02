package com.cipherlab.crypto.modern;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * SHA-256 cryptographic hash implementation using Java Cryptography Architecture
 */
public class SHA256Hash {
    
    private static final String ALGORITHM = "SHA-256";
    
    /**
     * Computes SHA-256 hash of the input
     * @param input The text to hash
     * @return Base64-encoded hash
     */
    public static String hash(String input) {
        try {
            if (input == null || input.isEmpty()) {
                return hash("");
            }
            
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }
    
    /**
     * Computes SHA-256 hash and returns as hexadecimal string
     * @param input The text to hash
     * @return Hexadecimal hash string
     */
    public static String hashHex(String input) {
        try {
            if (input == null || input.isEmpty()) {
                return hashHex("");
            }
            
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }
    
    /**
     * Verifies if the input matches the given hash
     * @param input The text to verify
     * @param hash Base64-encoded hash to compare against
     * @return true if the hash matches, false otherwise
     */
    public static boolean verify(String input, String hash) {
        String computedHash = hash(input);
        return computedHash.equals(hash);
    }
    
    /**
     * Verifies if the input matches the given hexadecimal hash
     * @param input The text to verify
     * @param hashHex Hexadecimal hash to compare against
     * @return true if the hash matches, false otherwise
     */
    public static boolean verifyHex(String input, String hashHex) {
        String computedHash = hashHex(input);
        return computedHash.equalsIgnoreCase(hashHex);
    }
    
    /**
     * Gets the hash length in bytes
     */
    public static int getHashLength() {
        return 32; // SHA-256 produces 256-bit = 32-byte hash
    }
}
