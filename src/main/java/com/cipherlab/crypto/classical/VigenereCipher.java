package com.cipherlab.crypto.classical;

/**
 * Vigenère Cipher implementation
 * A polyalphabetic substitution cipher using a keyword
 */
public class VigenereCipher {
    
    /**
     * Encrypts plaintext using Vigenère cipher
     * @param plaintext The text to encrypt
     * @param keyword The keyword for encryption
     * @return Encrypted ciphertext
     * @throws IllegalArgumentException if keyword is empty or contains no letters
     */
    public static String encrypt(String plaintext, String keyword) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty");
        }
        
        String cleanKeyword = keyword.replaceAll("[^a-zA-Z]", "");
        if (cleanKeyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must contain at least one letter");
        }
        
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int plainShift = c - base;
                int keyShift = Character.toUpperCase(cleanKeyword.charAt(keyIndex % cleanKeyword.length())) - 'A';
                int encrypted = (plainShift + keyShift) % 26;
                result.append((char) (encrypted + base));
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts ciphertext using Vigenère cipher
     * @param ciphertext The text to decrypt
     * @param keyword The keyword for decryption
     * @return Decrypted plaintext
     * @throws IllegalArgumentException if keyword is empty or contains no letters
     */
    public static String decrypt(String ciphertext, String keyword) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }
        
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty");
        }
        
        String cleanKeyword = keyword.replaceAll("[^a-zA-Z]", "");
        if (cleanKeyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword must contain at least one letter");
        }
        
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        
        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int cipherShift = c - base;
                int keyShift = Character.toUpperCase(cleanKeyword.charAt(keyIndex % cleanKeyword.length())) - 'A';
                int decrypted = (cipherShift - keyShift + 26) % 26;
                result.append((char) (decrypted + base));
                keyIndex++;
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
}
