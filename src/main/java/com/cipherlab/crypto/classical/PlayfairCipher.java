package com.cipherlab.crypto.classical;

import java.util.HashMap;
import java.util.Map;

/**
 * Playfair Cipher implementation
 * A digraph substitution cipher using a 5x5 key matrix
 */
public class PlayfairCipher {
    
    private static final char[][] MATRIX = new char[5][5];
    
    /**
     * Encrypts plaintext using Playfair cipher
     * @param plaintext The text to encrypt
     * @param keyword The keyword for the key matrix
     * @return Encrypted ciphertext
     */
    public static String encrypt(String plaintext, String keyword) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty");
        }
        
        createMatrix(keyword);
        String processed = preprocessText(plaintext);
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < processed.length(); i += 2) {
            char a = processed.charAt(i);
            char b = processed.charAt(i + 1);
            
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            
            if (posA[0] == posB[0]) {
                // Same row: shift right
                result.append(MATRIX[posA[0]][(posA[1] + 1) % 5]);
                result.append(MATRIX[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                // Same column: shift down
                result.append(MATRIX[(posA[0] + 1) % 5][posA[1]]);
                result.append(MATRIX[(posB[0] + 1) % 5][posB[1]]);
            } else {
                // Rectangle: swap columns
                result.append(MATRIX[posA[0]][posB[1]]);
                result.append(MATRIX[posB[0]][posA[1]]);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts ciphertext using Playfair cipher
     * @param ciphertext The text to decrypt
     * @param keyword The keyword for the key matrix
     * @return Decrypted plaintext
     */
    public static String decrypt(String ciphertext, String keyword) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }
        
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be empty");
        }
        
        createMatrix(keyword);
        String processed = preprocessText(ciphertext);
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < processed.length(); i += 2) {
            char a = processed.charAt(i);
            char b = processed.charAt(i + 1);
            
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            
            if (posA[0] == posB[0]) {
                // Same row: shift left
                result.append(MATRIX[posA[0]][(posA[1] + 4) % 5]);
                result.append(MATRIX[posB[0]][(posB[1] + 4) % 5]);
            } else if (posA[1] == posB[1]) {
                // Same column: shift up
                result.append(MATRIX[(posA[0] + 4) % 5][posA[1]]);
                result.append(MATRIX[(posB[0] + 4) % 5][posB[1]]);
            } else {
                // Rectangle: swap columns
                result.append(MATRIX[posA[0]][posB[1]]);
                result.append(MATRIX[posB[0]][posA[1]]);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Creates the 5x5 key matrix from the keyword
     */
    private static void createMatrix(String keyword) {
        String cleanKeyword = keyword.toUpperCase().replaceAll("[^A-Z]", "");
        StringBuilder alphabet = new StringBuilder("ABCDEFGHIKLMNOPQRSTUVWXYZ"); // I/J combined
        
        // Remove duplicate letters from keyword
        StringBuilder uniqueKeyword = new StringBuilder();
        Map<Character, Boolean> seen = new HashMap<>();
        
        for (char c : cleanKeyword.toCharArray()) {
            if (!seen.containsKey(c)) {
                seen.put(c, true);
                uniqueKeyword.append(c);
                alphabet = new StringBuilder(alphabet.toString().replace(String.valueOf(c), ""));
            }
        }
        
        String keyString = uniqueKeyword.toString() + alphabet.toString();
        
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                MATRIX[i][j] = keyString.charAt(index++);
            }
        }
    }
    
    /**
     * Finds the position of a character in the matrix
     */
    private static int[] findPosition(char c) {
        c = Character.toUpperCase(c);
        if (c == 'J') c = 'I';
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (MATRIX[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("Character not found in matrix: " + c);
    }
    
    /**
     * Preprocesses text: uppercase, remove non-letters, insert X for double letters, pad if odd
     */
    private static String preprocessText(String text) {
        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        
        StringBuilder processed = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            processed.append(text.charAt(i));
            if (i + 1 < text.length() && text.charAt(i) == text.charAt(i + 1)) {
                processed.append('X');
            }
        }
        
        if (processed.length() % 2 != 0) {
            processed.append('X');
        }
        
        return processed.toString();
    }
}
