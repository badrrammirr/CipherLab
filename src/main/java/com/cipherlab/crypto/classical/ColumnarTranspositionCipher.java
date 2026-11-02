package com.cipherlab.crypto.classical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Columnar Transposition Cipher implementation
 * A transposition cipher that rearranges the message based on a keyword
 */
public class ColumnarTranspositionCipher {
    
    /**
     * Encrypts plaintext using Columnar Transposition cipher
     * @param plaintext The text to encrypt
     * @param keyword The keyword for column ordering
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
        
        String cleanText = plaintext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        int cols = cleanKeyword.length();
        int rows = (int) Math.ceil((double) cleanText.length() / cols);
        
        // Fill the grid
        char[][] grid = new char[rows][cols];
        int index = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (index < cleanText.length()) {
                    grid[i][j] = cleanText.charAt(index++);
                } else {
                    grid[i][j] = 'X'; // Padding
                }
            }
        }
        
        // Determine column order based on keyword
        List<ColumnOrder> columnOrders = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            columnOrders.add(new ColumnOrder(i, Character.toUpperCase(cleanKeyword.charAt(i))));
        }
        Collections.sort(columnOrders);
        
        // Read columns in order
        StringBuilder result = new StringBuilder();
        for (ColumnOrder co : columnOrders) {
            for (int i = 0; i < rows; i++) {
                result.append(grid[i][co.originalIndex]);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts ciphertext using Columnar Transposition cipher
     * @param ciphertext The text to decrypt
     * @param keyword The keyword for column ordering
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
        
        String cleanText = ciphertext.replaceAll("[^a-zA-Z]", "").toUpperCase();
        
        int cols = cleanKeyword.length();
        int rows = (int) Math.ceil((double) cleanText.length() / cols);
        
        // Determine column order based on keyword
        List<ColumnOrder> columnOrders = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            columnOrders.add(new ColumnOrder(i, Character.toUpperCase(cleanKeyword.charAt(i))));
        }
        Collections.sort(columnOrders);
        
        // Calculate column lengths
        int[] colLengths = new int[cols];
        int baseLength = cleanText.length() / cols;
        int extra = cleanText.length() % cols;
        
        for (int i = 0; i < cols; i++) {
            colLengths[i] = baseLength + (i < extra ? 1 : 0);
        }
        
        // Fill columns from ciphertext
        char[][] columns = new char[cols][rows];
        int index = 0;
        for (ColumnOrder co : columnOrders) {
            int len = colLengths[co.originalIndex];
            for (int i = 0; i < len; i++) {
                if (index < cleanText.length()) {
                    columns[co.originalIndex][i] = cleanText.charAt(index++);
                }
            }
        }
        
        // Read grid row by row
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i < colLengths[j]) {
                    result.append(columns[j][i]);
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * Helper class for column ordering
     */
    private static class ColumnOrder implements Comparable<ColumnOrder> {
        int originalIndex;
        char character;
        
        ColumnOrder(int originalIndex, char character) {
            this.originalIndex = originalIndex;
            this.character = character;
        }
        
        @Override
        public int compareTo(ColumnOrder other) {
            return Character.compare(this.character, other.character);
        }
    }
}
