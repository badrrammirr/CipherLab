package com.cipherlab.crypto.classical;

/**
 * Rail Fence Cipher implementation
 * A transposition cipher that writes the message in a zigzag pattern
 */
public class RailFenceCipher {
    
    /**
     * Encrypts plaintext using Rail Fence cipher
     * @param plaintext The text to encrypt
     * @param rails Number of rails (must be >= 2)
     * @return Encrypted ciphertext
     * @throws IllegalArgumentException if rails < 2
     */
    public static String encrypt(String plaintext, int rails) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        
        if (rails < 2) {
            throw new IllegalArgumentException("Number of rails must be at least 2");
        }
        
        if (rails >= plaintext.length()) {
            return plaintext;
        }
        
        StringBuilder[] fence = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            fence[i] = new StringBuilder();
        }
        
        int rail = 0;
        int direction = 1;
        
        for (char c : plaintext.toCharArray()) {
            fence[rail].append(c);
            
            if (rail == 0) {
                direction = 1;
            } else if (rail == rails - 1) {
                direction = -1;
            }
            
            rail += direction;
        }
        
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : fence) {
            result.append(sb);
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts ciphertext using Rail Fence cipher
     * @param ciphertext The text to decrypt
     * @param rails Number of rails (must be >= 2)
     * @return Decrypted plaintext
     * @throws IllegalArgumentException if rails < 2
     */
    public static String decrypt(String ciphertext, int rails) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }
        
        if (rails < 2) {
            throw new IllegalArgumentException("Number of rails must be at least 2");
        }
        
        if (rails >= ciphertext.length()) {
            return ciphertext;
        }
        
        int n = ciphertext.length();
        int[] railLengths = new int[rails];
        
        int rail = 0;
        int direction = 1;
        
        for (int i = 0; i < n; i++) {
            railLengths[rail]++;
            
            if (rail == 0) {
                direction = 1;
            } else if (rail == rails - 1) {
                direction = -1;
            }
            
            rail += direction;
        }
        
        StringBuilder[] fence = new StringBuilder[rails];
        int index = 0;
        for (int i = 0; i < rails; i++) {
            fence[i] = new StringBuilder(ciphertext.substring(index, index + railLengths[i]));
            index += railLengths[i];
        }
        
        StringBuilder result = new StringBuilder();
        rail = 0;
        direction = 1;
        int[] positions = new int[rails];
        
        for (int i = 0; i < n; i++) {
            result.append(fence[rail].charAt(positions[rail]++));
            
            if (rail == 0) {
                direction = 1;
            } else if (rail == rails - 1) {
                direction = -1;
            }
            
            rail += direction;
        }
        
        return result.toString();
    }
}
