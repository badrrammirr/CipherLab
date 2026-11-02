package com.cipherlab.crypto.modern;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES (Advanced Encryption Standard) implementation using Java Cryptography Architecture
 * Supports AES-256 in GCM mode for authenticated encryption
 */
public class AESCipher {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;
    
    /**
     * Generates a new AES-256 key
     * @return Base64-encoded key
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AES key", e);
        }
    }
    
    /**
     * Encrypts plaintext using AES-256-GCM
     * @param plaintext The text to encrypt
     * @param key Base64-encoded AES key
     * @return Base64-encoded ciphertext (IV + ciphertext)
     * @throws IllegalArgumentException if key is invalid
     */
    public static String encrypt(String plaintext, String key) {
        try {
            if (plaintext == null || plaintext.isEmpty()) {
                return plaintext;
            }
            
            byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            
            // Generate random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
            
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // Combine IV and ciphertext
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
            
            return Base64.getEncoder().encodeToString(combined);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AES key", e);
        } catch (Exception e) {
            throw new RuntimeException("AES encryption failed", e);
        }
    }
    
    /**
     * Decrypts ciphertext using AES-256-GCM
     * @param ciphertext Base64-encoded ciphertext (IV + ciphertext)
     * @param key Base64-encoded AES key
     * @return Decrypted plaintext
     * @throws IllegalArgumentException if key or ciphertext is invalid
     */
    public static String decrypt(String ciphertext, String key) {
        try {
            if (ciphertext == null || ciphertext.isEmpty()) {
                return ciphertext;
            }
            
            byte[] keyBytes = Base64.getDecoder().decode(key);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, ALGORITHM);
            
            byte[] combined = Base64.getDecoder().decode(ciphertext);
            
            // Extract IV and ciphertext
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encryptedBytes = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encryptedBytes, 0, encryptedBytes.length);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid AES key or ciphertext", e);
        } catch (Exception e) {
            throw new RuntimeException("AES decryption failed", e);
        }
    }
    
    /**
     * Gets the key size in bits
     */
    public static int getKeySize() {
        return KEY_SIZE;
    }
}
