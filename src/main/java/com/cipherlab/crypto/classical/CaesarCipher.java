package com.cipherlab.crypto.classical;

/**
 * Caesar Cipher implementation
 * A substitution cipher where each letter is shifted by a fixed number of positions
 */
public class CaesarCipher {
    
    /**
     * Encrypts plaintext using Caesar cipher
     * @param plaintext The text to encrypt
     * @param shift The shift value (0-25)
     * @return Encrypted ciphertext
     */
    public static String encrypt(String plaintext, int shift) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        
        shift = normalizeShift(shift);
        StringBuilder result = new StringBuilder();
        
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char shifted = (char) ((c - base + shift) % 26 + base);
                result.append(shifted);
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts ciphertext using Caesar cipher
     * @param ciphertext The text to decrypt
     * @param shift The shift value (0-25)
     * @return Decrypted plaintext
     */
    public static String decrypt(String ciphertext, int shift) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }
        
        shift = normalizeShift(shift);
        return encrypt(ciphertext, 26 - shift);
    }
    
    /**
     * Normalizes shift to be within 0-25 range
     */
    private static int normalizeShift(int shift) {
        shift = shift % 26;
        if (shift < 0) {
            shift += 26;
        }
        return shift;
    }
    
    /**
     * Performs brute-force attack on Caesar cipher
     * @param ciphertext The ciphertext to attack
     * @return Array of all possible plaintexts (26 possibilities)
     */
    public static String[] bruteForce(String ciphertext) {
        String[] results = new String[26];
        for (int i = 0; i < 26; i++) {
            results[i] = decrypt(ciphertext, i);
        }
        return results;
    }
}
