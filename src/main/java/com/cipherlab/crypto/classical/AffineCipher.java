package com.cipherlab.crypto.classical;

/**
 * Affine Cipher implementation
 * A substitution cipher using the formula: E(x) = (ax + b) mod 26
 * where a and b are keys, and a must be coprime with 26
 */
public class AffineCipher {
    
    private static final int MOD = 26;
    
    /**
     * Encrypts plaintext using Affine cipher
     * @param plaintext The text to encrypt
     * @param a Key a (must be coprime with 26)
     * @param b Key b
     * @return Encrypted ciphertext
     * @throws IllegalArgumentException if a is not coprime with 26
     */
    public static String encrypt(String plaintext, int a, int b) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }
        
        if (!isValidKeyA(a)) {
            throw new IllegalArgumentException("Key 'a' must be coprime with 26. Valid values: 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25");
        }
        
        a = normalizeKey(a);
        b = normalizeKey(b);
        
        StringBuilder result = new StringBuilder();
        
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int x = c - base;
                int encrypted = (a * x + b) % MOD;
                result.append((char) (encrypted + base));
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Decrypts ciphertext using Affine cipher
     * @param ciphertext The text to decrypt
     * @param a Key a (must be coprime with 26)
     * @param b Key b
     * @return Decrypted plaintext
     * @throws IllegalArgumentException if a is not coprime with 26
     */
    public static String decrypt(String ciphertext, int a, int b) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }
        
        if (!isValidKeyA(a)) {
            throw new IllegalArgumentException("Key 'a' must be coprime with 26. Valid values: 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25");
        }
        
        a = normalizeKey(a);
        b = normalizeKey(b);
        
        int aInverse = modInverse(a, MOD);
        
        StringBuilder result = new StringBuilder();
        
        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                int y = c - base;
                int decrypted = (aInverse * (y - b + MOD)) % MOD;
                result.append((char) (decrypted + base));
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Calculates modular inverse using Extended Euclidean Algorithm
     */
    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        throw new IllegalArgumentException("No modular inverse exists");
    }
    
    /**
     * Checks if key a is valid (coprime with 26)
     */
    public static boolean isValidKeyA(int a) {
        a = normalizeKey(a);
        return gcd(a, MOD) == 1;
    }
    
    /**
     * Calculates greatest common divisor
     */
    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
    
    /**
     * Normalizes key to be within 0-25 range
     */
    private static int normalizeKey(int key) {
        key = key % MOD;
        if (key < 0) {
            key += MOD;
        }
        return key;
    }
    
    /**
     * Gets all valid values for key a
     */
    public static int[] getValidKeyAValues() {
        return new int[]{1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25};
    }
    
    /**
     * Performs brute-force attack on Affine cipher
     * @param ciphertext The ciphertext to attack
     * @return Array of all possible plaintexts (312 possibilities: 12 * 26)
     */
    public static String[] bruteForce(String ciphertext) {
        int[] validA = getValidKeyAValues();
        String[] results = new String[validA.length * 26];
        int index = 0;
        
        for (int a : validA) {
            for (int b = 0; b < 26; b++) {
                try {
                    results[index] = decrypt(ciphertext, a, b);
                } catch (Exception e) {
                    results[index] = "Error: " + e.getMessage();
                }
                index++;
            }
        }
        
        return results;
    }
}
