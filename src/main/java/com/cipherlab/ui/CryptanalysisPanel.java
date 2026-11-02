package com.cipherlab.ui;

import com.cipherlab.crypto.classical.*;

import javax.swing.*;
import java.awt.*;

/**
 * Module 4: Cryptanalysis and Vulnerability Testing
 */
public class CryptanalysisPanel extends JPanel {
    
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(25, 25, 25);
    private static final Color ACCENT_COLOR = new Color(0, 150, 200);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    private JComboBox<String> attackTypeCombo;
    private JTextArea ciphertextArea;
    private JTextArea resultsArea;
    private JTextField knownPlaintextField;
    private JTextField knownCiphertextField;
    
    public CryptanalysisPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BG);
        initializeUI();
    }
    
    private void initializeUI() {
        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(DARK_BG);
        splitPane.setDividerLocation(400);
        
        // Left panel: Input and controls
        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);
        
        // Right panel: Results
        JPanel rightPanel = createRightPanel();
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Cryptanalysis & Vulnerability Testing");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Attack selection
        JPanel attackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        attackPanel.setBackground(DARK_BG);
        
        JLabel attackLabel = new JLabel("Attack Type:");
        attackLabel.setForeground(TEXT_COLOR);
        
        attackTypeCombo = new JComboBox<>(new String[]{
            "Caesar Brute-Force",
            "Affine Brute-Force",
            "Frequency Analysis",
            "Known-Plaintext Attack"
        });
        attackTypeCombo.setBackground(DARKER_BG);
        attackTypeCombo.setForeground(TEXT_COLOR);
        attackTypeCombo.addActionListener(e -> updateInputFields());
        
        attackPanel.add(attackLabel);
        attackPanel.add(attackTypeCombo);
        
        // Ciphertext input
        JPanel cipherPanel = new JPanel(new BorderLayout(5, 5));
        cipherPanel.setBackground(DARK_BG);
        
        JLabel cipherLabel = new JLabel("Ciphertext to Attack:");
        cipherLabel.setForeground(TEXT_COLOR);
        
        ciphertextArea = new JTextArea(8, 30);
        ciphertextArea.setBackground(DARKER_BG);
        ciphertextArea.setForeground(TEXT_COLOR);
        ciphertextArea.setLineWrap(true);
        ciphertextArea.setWrapStyleWord(true);
        
        JScrollPane cipherScroll = new JScrollPane(ciphertextArea);
        cipherScroll.setBackground(DARKER_BG);
        
        cipherPanel.add(cipherLabel, BorderLayout.NORTH);
        cipherPanel.add(cipherScroll, BorderLayout.CENTER);
        
        // Known plaintext/ciphertext fields (for known-plaintext attack)
        JPanel knownPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        knownPanel.setBackground(DARK_BG);
        
        JLabel knownPlainLabel = new JLabel("Known Plaintext:");
        knownPlainLabel.setForeground(TEXT_COLOR);
        knownPlaintextField = new JTextField();
        knownPlaintextField.setBackground(DARKER_BG);
        knownPlaintextField.setForeground(TEXT_COLOR);
        
        JLabel knownCipherLabel = new JLabel("Known Ciphertext:");
        knownCipherLabel.setForeground(TEXT_COLOR);
        knownCiphertextField = new JTextField();
        knownCiphertextField.setBackground(DARKER_BG);
        knownCiphertextField.setForeground(TEXT_COLOR);
        
        knownPanel.add(knownPlainLabel);
        knownPanel.add(knownPlaintextField);
        knownPanel.add(knownCipherLabel);
        knownPanel.add(knownCiphertextField);
        
        // Attack button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(DARK_BG);
        
        JButton attackBtn = new JButton("Launch Attack");
        attackBtn.setBackground(ACCENT_COLOR);
        attackBtn.setForeground(Color.WHITE);
        attackBtn.setPreferredSize(new Dimension(150, 40));
        attackBtn.addActionListener(e -> performAttack());
        
        JButton clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(150, 50, 50));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setPreferredSize(new Dimension(100, 40));
        clearBtn.addActionListener(e -> clearFields());
        
        buttonPanel.add(attackBtn);
        buttonPanel.add(clearBtn);
        
        // Layout
        JPanel leftContent = new JPanel(new BorderLayout(10, 10));
        leftContent.setBackground(DARK_BG);
        leftContent.add(attackPanel, BorderLayout.NORTH);
        leftContent.add(cipherPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(DARK_BG);
        bottomPanel.add(knownPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        leftContent.add(bottomPanel, BorderLayout.SOUTH);
        
        panel.add(leftContent, BorderLayout.CENTER);
        
        updateInputFields();
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel resultsLabel = new JLabel("Attack Results:");
        resultsLabel.setForeground(TEXT_COLOR);
        resultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        resultsArea = new JTextArea();
        resultsArea.setBackground(DARKER_BG);
        resultsArea.setForeground(TEXT_COLOR);
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane resultsScroll = new JScrollPane(resultsArea);
        resultsScroll.setBackground(DARKER_BG);
        
        panel.add(resultsLabel, BorderLayout.NORTH);
        panel.add(resultsScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void updateInputFields() {
        String attackType = (String) attackTypeCombo.getSelectedItem();
        
        boolean showKnownFields = attackType.equals("Known-Plaintext Attack");
        knownPlaintextField.setEnabled(showKnownFields);
        knownCiphertextField.setEnabled(showKnownFields);
    }
    
    private void performAttack() {
        String attackType = (String) attackTypeCombo.getSelectedItem();
        String ciphertext = ciphertextArea.getText().trim();
        
        if (ciphertext.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter ciphertext to attack.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        resultsArea.setText("");
        
        switch (attackType) {
            case "Caesar Brute-Force":
                caesarBruteForce(ciphertext);
                break;
            case "Affine Brute-Force":
                affineBruteForce(ciphertext);
                break;
            case "Frequency Analysis":
                frequencyAnalysis(ciphertext);
                break;
            case "Known-Plaintext Attack":
                knownPlaintextAttack(ciphertext);
                break;
        }
    }
    
    private void caesarBruteForce(String ciphertext) {
        StringBuilder results = new StringBuilder();
        results.append("CAESAR BRUTE-FORCE ATTACK\n");
        results.append("========================\n");
        results.append("Testing all 26 possible shifts...\n\n");
        
        String[] possibilities = CaesarCipher.bruteForce(ciphertext);
        
        for (int i = 0; i < possibilities.length; i++) {
            results.append(String.format("Shift %2d: %s\n", i, possibilities[i]));
        }
        
        results.append("\nAnalysis:\n");
        results.append("Caesar cipher has a key space of only 26 possible keys.\n");
        results.append("This makes it extremely vulnerable to brute-force attacks.\n");
        results.append("The correct plaintext can be identified by looking for\n");
        results.append("meaningful English words or by using frequency analysis.\n");
        
        resultsArea.setText(results.toString());
    }
    
    private void affineBruteForce(String ciphertext) {
        StringBuilder results = new StringBuilder();
        results.append("AFFINE BRUTE-FORCE ATTACK\n");
        results.append("=========================\n");
        results.append("Testing all valid key combinations...\n");
        results.append("(12 valid 'a' keys × 26 'b' keys = 312 possibilities)\n\n");
        
        String[] possibilities = AffineCipher.bruteForce(ciphertext);
        
        int count = 0;
        for (int i = 0; i < possibilities.length; i++) {
            if (!possibilities[i].startsWith("Error")) {
                int a = AffineCipher.getValidKeyAValues()[i / 26];
                int b = i % 26;
                results.append(String.format("a=%d, b=%d: %s\n", a, b, possibilities[i]));
                count++;
                
                if (count >= 50) {
                    results.append("... (showing first 50 results)\n");
                    break;
                }
            }
        }
        
        results.append("\nAnalysis:\n");
        results.append("Affine cipher has a key space of 312 possible keys.\n");
        results.append("Key 'a' must be coprime with 26 (valid values: 1,3,5,7,9,11,15,17,19,21,23,25).\n");
        results.append("While larger than Caesar, it's still vulnerable to brute-force.\n");
        
        resultsArea.setText(results.toString());
    }
    
    private void frequencyAnalysis(String ciphertext) {
        StringBuilder results = new StringBuilder();
        results.append("FREQUENCY ANALYSIS\n");
        results.append("===================\n\n");
        
        // Count letter frequencies
        int[] counts = new int[26];
        int total = 0;
        
        for (char c : ciphertext.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                counts[c - 'A']++;
                total++;
            }
        }
        
        results.append("Letter Frequency in Ciphertext:\n");
        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) {
                double pct = (counts[i] * 100.0) / total;
                results.append(String.format("  %c: %d (%.2f%%)\n", 
                    (char) ('A' + i), counts[i], pct));
            }
        }
        
        results.append("\nMost Frequent Letters:\n");
        for (int i = 0; i < 5; i++) {
            int maxIdx = 0;
            for (int j = 1; j < 26; j++) {
                if (counts[j] > counts[maxIdx]) {
                    maxIdx = j;
                }
            }
            if (counts[maxIdx] > 0) {
                double pct = (counts[maxIdx] * 100.0) / total;
                results.append(String.format("  %d. %c (%.2f%%)\n", 
                    i + 1, (char) ('A' + maxIdx), pct));
                counts[maxIdx] = -1; // Mark as processed
            }
        }
        
        results.append("\nAnalysis:\n");
        results.append("In English, the most common letters are E, T, A, O, I, N, S, H, R.\n");
        results.append("If the ciphertext shows a different distribution, it may indicate:\n");
        results.append("  - A substitution cipher (letters are mapped to different letters)\n");
        results.append("  - A transposition cipher (letter frequencies are preserved)\n");
        results.append("  - A polyalphabetic cipher (frequencies are flattened)\n");
        results.append("\nFor Caesar cipher: If 'X' is most frequent, it likely maps to 'E'.\n");
        results.append("This suggests a shift of: (X - E) mod 26\n");
        
        resultsArea.setText(results.toString());
    }
    
    private void knownPlaintextAttack(String ciphertext) {
        String knownPlain = knownPlaintextField.getText().trim();
        String knownCipher = knownCiphertextField.getText().trim();
        
        if (knownPlain.isEmpty() || knownCipher.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both known plaintext and ciphertext.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder results = new StringBuilder();
        results.append("KNOWN-PLAINTEXT ATTACK\n");
        results.append("=======================\n\n");
        results.append("Known Plaintext: ").append(knownPlain).append("\n");
        results.append("Known Ciphertext: ").append(knownCipher).append("\n\n");
        
        // Try to determine if it's Caesar
        if (knownPlain.length() >= 1 && knownCipher.length() >= 1) {
            char p = Character.toUpperCase(knownPlain.charAt(0));
            char c = Character.toUpperCase(knownCipher.charAt(0));
            
            if (Character.isLetter(p) && Character.isLetter(c)) {
                int shift = (c - p + 26) % 26;
                results.append("Possible Caesar Shift: ").append(shift).append("\n");
                results.append("Verification: ").append(CaesarCipher.encrypt(knownPlain, shift)).append("\n");
                
                if (CaesarCipher.encrypt(knownPlain, shift).equalsIgnoreCase(knownCipher)) {
                    results.append("✓ Caesar cipher confirmed with shift ").append(shift).append("\n");
                }
            }
        }
        
        results.append("\nAnalysis:\n");
        results.append("Known-plaintext attacks exploit the relationship between\n");
        results.append("plaintext and ciphertext to determine the key.\n");
        results.append("\nFor classical ciphers:\n");
        results.append("  - Caesar: One known pair reveals the shift\n");
        results.append("  - Affine: Two known pairs can solve the equations\n");
        results.append("  - Vigenère: Known plaintext can reveal the key length\n");
        results.append("  - Substitution: Known pairs build the substitution table\n");
        results.append("\nModern ciphers (AES, RSA) are designed to resist\n");
        results.append("known-plaintext attacks through diffusion and confusion.\n");
        
        resultsArea.setText(results.toString());
    }
    
    private void clearFields() {
        ciphertextArea.setText("");
        knownPlaintextField.setText("");
        knownCiphertextField.setText("");
        resultsArea.setText("");
    }
}
