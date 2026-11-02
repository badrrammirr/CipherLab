package com.cipherlab.crypto.modern;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA (Rivest-Shamir-Adleman) implementation using Java Cryptography Architecture
 * Asymmetric encryption with 2048-bit keys
 */
public class RSACipher {
    
    private static final String ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    
    /**
     * Generates a new RSA key pair
     * @return KeyPair object containing public and private keys
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(KEY_SIZE);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }
    
    /**
     * Encodes a public key to Base64 string
     */
    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
    
    /**
     * Encodes a private key to Base64 string
     */
    public static String encodePrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }
    
    /**
     * Decodes a Base64 string to public key
     */
    public static PublicKey decodePublicKey(String encodedKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePublic(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode public key", e);
        }
    }
    
    /**
     * Decodes a Base64 string to private key
     */
    public static PrivateKey decodePrivateKey(String encodedKey) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            return keyFactory.generatePrivate(spec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode private key", e);
        }
    }
    
    /**
     * Encrypts plaintext using RSA public key
     * @param plaintext The text to encrypt
     * @param publicKey Base64-encoded public key
     * @return Base64-encoded ciphertext
     * @throws IllegalArgumentException if key is invalid or plaintext too long
     */
    public static String encrypt(String plaintext, String publicKey) {
        try {
            if (plaintext == null || plaintext.isEmpty()) {
                return plaintext;
            }
            
            PublicKey pubKey = decodePublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("RSA encryption failed", e);
        }
    }
    
    /**
     * Decrypts ciphertext using RSA private key
     * @param ciphertext Base64-encoded ciphertext
     * @param privateKey Base64-encoded private key
     * @return Decrypted plaintext
     * @throws IllegalArgumentException if key or ciphertext is invalid
     */
    public static String decrypt(String ciphertext, String privateKey) {
        try {
            if (ciphertext == null || ciphertext.isEmpty()) {
                return ciphertext;
            }
            
            PrivateKey privKey = decodePrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("RSA decryption failed", e);
        }
    }
    
    /**
     * Gets the key size in bits
     */
    public static int getKeySize() {
        return KEY_SIZE;
    }
}
