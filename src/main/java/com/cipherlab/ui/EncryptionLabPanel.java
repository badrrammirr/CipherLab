package com.cipherlab.ui;

import com.cipherlab.crypto.classical.*;
import com.cipherlab.crypto.modern.*;

import javax.swing.*;
import java.awt.*;
import java.security.KeyPair;

/**
 * Module 1: Encryption and Decryption Laboratory
 */
public class EncryptionLabPanel extends JPanel {
    
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(25, 25, 25);
    private static final Color ACCENT_COLOR = new Color(0, 150, 200);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    private JComboBox<String> algorithmCombo;
    private JTextArea plaintextArea;
    private JTextArea ciphertextArea;
    private JTextField keyField1;
    private JTextField keyField2;
    private JLabel keyLabel1;
    private JLabel keyLabel2;
    private JLabel keyLabel3;
    
    private String aesKey;
    private String rsaPublicKey;
    private String rsaPrivateKey;
    
    public EncryptionLabPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BG);
        initializeUI();
    }
    
    private void initializeUI() {
        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Input section
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Encryption / Decryption Laboratory");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(DARK_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Algorithm selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(createLabel("Select Algorithm:"), gbc);
        
        gbc.gridx = 2;
        gbc.gridwidth = 3;
        algorithmCombo = new JComboBox<>(new String[]{
            "Caesar Cipher",
            "Affine Cipher",
            "Vigenère Cipher",
            "Playfair Cipher",
            "Rail Fence Cipher",
            "Columnar Transposition",
            "AES-256",
            "RSA-2048",
            "SHA-256"
        });
        algorithmCombo.setBackground(DARKER_BG);
        algorithmCombo.setForeground(TEXT_COLOR);
        algorithmCombo.addActionListener(e -> updateKeyFields());
        panel.add(algorithmCombo, gbc);
        
        // Key fields
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        keyLabel1 = createLabel("Key 1:");
        panel.add(keyLabel1, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        keyField1 = new JTextField();
        keyField1.setBackground(DARKER_BG);
        keyField1.setForeground(TEXT_COLOR);
        keyField1.setPreferredSize(new Dimension(200, 30));
        panel.add(keyField1, gbc);
        
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        keyLabel2 = createLabel("Key 2:");
        panel.add(keyLabel2, gbc);
        
        gbc.gridx = 4;
        gbc.gridwidth = 1;
        keyField2 = new JTextField();
        keyField2.setBackground(DARKER_BG);
        keyField2.setForeground(TEXT_COLOR);
        keyField2.setPreferredSize(new Dimension(200, 30));
        panel.add(keyField2, gbc);
        
        // Generate key button for modern algorithms
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton generateKeyBtn = new JButton("Generate Keys");
        generateKeyBtn.setBackground(ACCENT_COLOR);
        generateKeyBtn.setForeground(Color.WHITE);
        generateKeyBtn.addActionListener(e -> generateKeys());
        panel.add(generateKeyBtn, gbc);
        
        // Plaintext
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        panel.add(createLabel("Plaintext:"), gbc);
        
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        plaintextArea = new JTextArea(8, 50);
        plaintextArea.setBackground(DARKER_BG);
        plaintextArea.setForeground(TEXT_COLOR);
        plaintextArea.setLineWrap(true);
        plaintextArea.setWrapStyleWord(true);
        JScrollPane plaintextScroll = new JScrollPane(plaintextArea);
        plaintextScroll.setBackground(DARKER_BG);
        panel.add(plaintextScroll, gbc);
        
        // Ciphertext
        gbc.gridy = 5;
        gbc.weighty = 0.0;
        panel.add(createLabel("Ciphertext / Hash:"), gbc);
        
        gbc.gridy = 6;
        gbc.weighty = 0.5;
        ciphertextArea = new JTextArea(8, 50);
        ciphertextArea.setBackground(DARKER_BG);
        ciphertextArea.setForeground(TEXT_COLOR);
        ciphertextArea.setLineWrap(true);
        ciphertextArea.setWrapStyleWord(true);
        ciphertextArea.setEditable(false);
        JScrollPane ciphertextScroll = new JScrollPane(ciphertextArea);
        ciphertextScroll.setBackground(DARKER_BG);
        panel.add(ciphertextScroll, gbc);
        
        updateKeyFields();
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(DARK_BG);
        
        JButton encryptBtn = new JButton("Encrypt / Hash");
        encryptBtn.setBackground(ACCENT_COLOR);
        encryptBtn.setForeground(Color.WHITE);
        encryptBtn.setPreferredSize(new Dimension(150, 40));
        encryptBtn.addActionListener(e -> performEncryption());
        
        JButton decryptBtn = new JButton("Decrypt / Verify");
        decryptBtn.setBackground(ACCENT_COLOR);
        decryptBtn.setForeground(Color.WHITE);
        decryptBtn.setPreferredSize(new Dimension(150, 40));
        decryptBtn.addActionListener(e -> performDecryption());
        
        JButton copyBtn = new JButton("Copy Result");
        copyBtn.setBackground(new Color(100, 100, 100));
        copyBtn.setForeground(Color.WHITE);
        copyBtn.setPreferredSize(new Dimension(120, 40));
        copyBtn.addActionListener(e -> copyResult());
        
        JButton clearBtn = new JButton("Clear");
        clearBtn.setBackground(new Color(150, 50, 50));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setPreferredSize(new Dimension(100, 40));
        clearBtn.addActionListener(e -> clearFields());
        
        panel.add(encryptBtn);
        panel.add(decryptBtn);
        panel.add(copyBtn);
        panel.add(clearBtn);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }
    
    private void updateKeyFields() {
        String algorithm = (String) algorithmCombo.getSelectedItem();
        
        keyField1.setVisible(true);
        keyField2.setVisible(true);
        keyLabel1.setVisible(true);
        keyLabel2.setVisible(true);
        
        switch (algorithm) {
            case "Caesar Cipher":
                keyLabel1.setText("Shift (0-25):");
                keyLabel2.setText("");
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
            case "Affine Cipher":
                keyLabel1.setText("Key a (1,3,5,7,9,11,15,17,19,21,23,25):");
                keyLabel2.setText("Key b (0-25):");
                break;
            case "Vigenère Cipher":
                keyLabel1.setText("Keyword:");
                keyLabel2.setText("");
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
            case "Playfair Cipher":
                keyLabel1.setText("Keyword:");
                keyLabel2.setText("");
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
            case "Rail Fence Cipher":
                keyLabel1.setText("Rails (>=2):");
                keyLabel2.setText("");
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
            case "Columnar Transposition":
                keyLabel1.setText("Keyword:");
                keyLabel2.setText("");
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
            case "AES-256":
                keyLabel1.setText("AES Key (auto-generated or paste):");
                keyLabel2.setText("");
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
            case "RSA-2048":
                keyLabel1.setText("Public Key (auto-generated or paste):");
                keyLabel2.setText("Private Key (auto-generated or paste):");
                break;
            case "SHA-256":
                keyLabel1.setText("(No key needed)");
                keyLabel2.setText("");
                keyField1.setVisible(false);
                keyField2.setVisible(false);
                keyLabel2.setVisible(false);
                break;
        }
    }
    
    private void generateKeys() {
        String algorithm = (String) algorithmCombo.getSelectedItem();
        
        try {
            switch (algorithm) {
                case "AES-256":
                    aesKey = AESCipher.generateKey();
                    keyField1.setText(aesKey);
                    break;
                case "RSA-2048":
                    KeyPair keyPair = RSACipher.generateKeyPair();
                    rsaPublicKey = RSACipher.encodePublicKey(keyPair.getPublic());
                    rsaPrivateKey = RSACipher.encodePrivateKey(keyPair.getPrivate());
                    keyField1.setText(rsaPublicKey);
                    keyField2.setText(rsaPrivateKey);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, 
                        "Key generation is only available for AES and RSA algorithms.", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error generating keys: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performEncryption() {
        String algorithm = (String) algorithmCombo.getSelectedItem();
        String plaintext = plaintextArea.getText();
        
        try {
            String result = "";
            
            switch (algorithm) {
                case "Caesar Cipher":
                    int shift = Integer.parseInt(keyField1.getText());
                    result = CaesarCipher.encrypt(plaintext, shift);
                    break;
                case "Affine Cipher":
                    int a = Integer.parseInt(keyField1.getText());
                    int b = Integer.parseInt(keyField2.getText());
                    result = AffineCipher.encrypt(plaintext, a, b);
                    break;
                case "Vigenère Cipher":
                    String vigenereKey = keyField1.getText();
                    result = VigenereCipher.encrypt(plaintext, vigenereKey);
                    break;
                case "Playfair Cipher":
                    String playfairKey = keyField1.getText();
                    result = PlayfairCipher.encrypt(plaintext, playfairKey);
                    break;
                case "Rail Fence Cipher":
                    int rails = Integer.parseInt(keyField1.getText());
                    result = RailFenceCipher.encrypt(plaintext, rails);
                    break;
                case "Columnar Transposition":
                    String columnarKey = keyField1.getText();
                    result = ColumnarTranspositionCipher.encrypt(plaintext, columnarKey);
                    break;
                case "AES-256":
                    String aesKey = keyField1.getText();
                    result = AESCipher.encrypt(plaintext, aesKey);
                    break;
                case "RSA-2048":
                    String rsaPubKey = keyField1.getText();
                    result = RSACipher.encrypt(plaintext, rsaPubKey);
                    break;
                case "SHA-256":
                    result = SHA256Hash.hashHex(plaintext);
                    break;
            }
            
            ciphertextArea.setText(result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Encryption error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performDecryption() {
        String algorithm = (String) algorithmCombo.getSelectedItem();
        String ciphertext = ciphertextArea.getText();
        
        try {
            String result = "";
            
            switch (algorithm) {
                case "Caesar Cipher":
                    int shift = Integer.parseInt(keyField1.getText());
                    result = CaesarCipher.decrypt(ciphertext, shift);
                    break;
                case "Affine Cipher":
                    int a = Integer.parseInt(keyField1.getText());
                    int b = Integer.parseInt(keyField2.getText());
                    result = AffineCipher.decrypt(ciphertext, a, b);
                    break;
                case "Vigenère Cipher":
                    String vigenereKey = keyField1.getText();
                    result = VigenereCipher.decrypt(ciphertext, vigenereKey);
                    break;
                case "Playfair Cipher":
                    String playfairKey = keyField1.getText();
                    result = PlayfairCipher.decrypt(ciphertext, playfairKey);
                    break;
                case "Rail Fence Cipher":
                    int rails = Integer.parseInt(keyField1.getText());
                    result = RailFenceCipher.decrypt(ciphertext, rails);
                    break;
                case "Columnar Transposition":
                    String columnarKey = keyField1.getText();
                    result = ColumnarTranspositionCipher.decrypt(ciphertext, columnarKey);
                    break;
                case "AES-256":
                    String aesKey = keyField1.getText();
                    result = AESCipher.decrypt(ciphertext, aesKey);
                    break;
                case "RSA-2048":
                    String rsaPrivKey = keyField2.getText();
                    result = RSACipher.decrypt(ciphertext, rsaPrivKey);
                    break;
                case "SHA-256":
                    // Verification instead of decryption
                    String input = plaintextArea.getText();
                    String computedHash = SHA256Hash.hashHex(input);
                    if (computedHash.equalsIgnoreCase(ciphertext)) {
                        result = "Hash verification: SUCCESS - The hash matches the input.";
                    } else {
                        result = "Hash verification: FAILED - The hash does not match the input.";
                    }
                    break;
            }
            
            if (algorithm.equals("SHA-256")) {
                JOptionPane.showMessageDialog(this, result, "Verification Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                plaintextArea.setText(result);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Decryption error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void copyResult() {
        String result = ciphertextArea.getText();
        if (!result.isEmpty()) {
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new java.awt.datatransfer.StringSelection(result), null);
            JOptionPane.showMessageDialog(this, "Result copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearFields() {
        plaintextArea.setText("");
        ciphertextArea.setText("");
        keyField1.setText("");
        keyField2.setText("");
    }
}
