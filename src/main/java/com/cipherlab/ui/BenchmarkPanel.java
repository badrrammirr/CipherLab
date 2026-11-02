package com.cipherlab.ui;

import com.cipherlab.crypto.classical.*;
import com.cipherlab.crypto.modern.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

/**
 * Module 2: Performance Benchmarking
 */
public class BenchmarkPanel extends JPanel {
    
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(25, 25, 25);
    private static final Color ACCENT_COLOR = new Color(0, 150, 200);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    private JTextArea inputTextArea;
    private JComboBox<String> algorithmCombo;
    private JTextField iterationsField;
    private JTable resultsTable;
    private ChartPanel chartPanel;
    
    private String aesKey;
    private String rsaPublicKey;
    private String rsaPrivateKey;
    
    public BenchmarkPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BG);
        initializeUI();
    }
    
    private void initializeUI() {
        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setBackground(DARK_BG);
        splitPane.setDividerLocation(300);
        
        // Top panel: Input and controls
        JPanel topPanel = createTopPanel();
        splitPane.setTopComponent(topPanel);
        
        // Bottom panel: Results and chart
        JPanel bottomPanel = createBottomPanel();
        splitPane.setBottomComponent(bottomPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Performance Benchmarking");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Input section
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(DARK_BG);
        
        JLabel inputLabel = new JLabel("Input Text for Benchmarking:");
        inputLabel.setForeground(TEXT_COLOR);
        inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        inputTextArea = new JTextArea(5, 50);
        inputTextArea.setBackground(DARKER_BG);
        inputTextArea.setForeground(TEXT_COLOR);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setText("The quick brown fox jumps over the lazy dog. " +
            "This is a sample text for benchmarking cryptographic algorithms. " +
            "Performance measurements will help compare different encryption methods.");
        
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputScroll.setBackground(DARKER_BG);
        
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        
        // Controls section
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlsPanel.setBackground(DARK_BG);
        
        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setForeground(TEXT_COLOR);
        
        algorithmCombo = new JComboBox<>(new String[]{
            "All Algorithms",
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
        
        JLabel iterLabel = new JLabel("Iterations:");
        iterLabel.setForeground(TEXT_COLOR);
        
        iterationsField = new JTextField("10");
        iterationsField.setPreferredSize(new Dimension(80, 30));
        iterationsField.setBackground(DARKER_BG);
        iterationsField.setForeground(TEXT_COLOR);
        
        JButton benchmarkBtn = new JButton("Run Benchmark");
        benchmarkBtn.setBackground(ACCENT_COLOR);
        benchmarkBtn.setForeground(Color.WHITE);
        benchmarkBtn.setPreferredSize(new Dimension(150, 40));
        benchmarkBtn.addActionListener(e -> runBenchmark());
        
        JButton compareBtn = new JButton("Compare All");
        compareBtn.setBackground(new Color(100, 150, 100));
        compareBtn.setForeground(Color.WHITE);
        compareBtn.setPreferredSize(new Dimension(150, 40));
        compareBtn.addActionListener(e -> compareAllAlgorithms());
        
        controlsPanel.add(algoLabel);
        controlsPanel.add(algorithmCombo);
        controlsPanel.add(iterLabel);
        controlsPanel.add(iterationsField);
        controlsPanel.add(benchmarkBtn);
        controlsPanel.add(compareBtn);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(controlsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        // Results table
        String[] columnNames = {"Algorithm", "Encryption Time (ms)", "Decryption Time (ms)", "Avg Time (ms)"};
        resultsTable = new JTable(new Object[0][4], columnNames);
        resultsTable.setBackground(DARKER_BG);
        resultsTable.setForeground(TEXT_COLOR);
        resultsTable.getTableHeader().setBackground(new Color(20, 20, 20));
        resultsTable.getTableHeader().setForeground(TEXT_COLOR);
        resultsTable.setRowHeight(25);
        
        JScrollPane tableScroll = new JScrollPane(resultsTable);
        tableScroll.setPreferredSize(new Dimension(0, 150));
        tableScroll.setBackground(DARKER_BG);
        
        // Chart panel
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(DARK_BG);
        chartPanel = new ChartPanel(null);
        chartContainer.add(chartPanel, BorderLayout.CENTER);
        
        panel.add(tableScroll, BorderLayout.NORTH);
        panel.add(chartContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void runBenchmark() {
        String algorithm = (String) algorithmCombo.getSelectedItem();
        String input = inputTextArea.getText();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter input text for benchmarking.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int iterations = Integer.parseInt(iterationsField.getText());
            if (iterations < 1) {
                throw new NumberFormatException();
            }
            
            // Generate keys if needed
            if (aesKey == null) {
                aesKey = AESCipher.generateKey();
            }
            if (rsaPublicKey == null || rsaPrivateKey == null) {
                KeyPair keyPair = RSACipher.generateKeyPair();
                rsaPublicKey = RSACipher.encodePublicKey(keyPair.getPublic());
                rsaPrivateKey = RSACipher.encodePrivateKey(keyPair.getPrivate());
            }
            
            BenchmarkResult result = benchmarkAlgorithm(algorithm, input, iterations);
            displayResult(result);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number of iterations (>= 1).", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Benchmark error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void compareAllAlgorithms() {
        String input = inputTextArea.getText();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter input text for benchmarking.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int iterations = Integer.parseInt(iterationsField.getText());
            if (iterations < 1) {
                throw new NumberFormatException();
            }
            
            // Generate keys if needed
            if (aesKey == null) {
                aesKey = AESCipher.generateKey();
            }
            if (rsaPublicKey == null || rsaPrivateKey == null) {
                KeyPair keyPair = RSACipher.generateKeyPair();
                rsaPublicKey = RSACipher.encodePublicKey(keyPair.getPublic());
                rsaPrivateKey = RSACipher.encodePrivateKey(keyPair.getPrivate());
            }
            
            String[] algorithms = {
                "Caesar Cipher",
                "Affine Cipher",
                "Vigenère Cipher",
                "Playfair Cipher",
                "Rail Fence Cipher",
                "Columnar Transposition",
                "AES-256",
                "RSA-2048",
                "SHA-256"
            };
            
            BenchmarkResult[] results = new BenchmarkResult[algorithms.length];
            for (int i = 0; i < algorithms.length; i++) {
                results[i] = benchmarkAlgorithm(algorithms[i], input, iterations);
            }
            
            displayAllResults(results);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid number of iterations (>= 1).", 
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Benchmark error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private BenchmarkResult benchmarkAlgorithm(String algorithm, String input, int iterations) {
        long encryptTime = 0;
        long decryptTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            try {
                // Encryption
                long startEncrypt = System.nanoTime();
                String encrypted = encrypt(algorithm, input);
                long endEncrypt = System.nanoTime();
                encryptTime += (endEncrypt - startEncrypt);
                
                // Decryption (if applicable)
                if (!algorithm.equals("SHA-256")) {
                    long startDecrypt = System.nanoTime();
                    decrypt(algorithm, encrypted);
                    long endDecrypt = System.nanoTime();
                    decryptTime += (endDecrypt - startDecrypt);
                }
            } catch (Exception e) {
                // Skip failed iterations
            }
        }
        
        double avgEncryptMs = (encryptTime / 1_000_000.0) / iterations;
        double avgDecryptMs = (decryptTime / 1_000_000.0) / iterations;
        
        return new BenchmarkResult(algorithm, avgEncryptMs, avgDecryptMs);
    }
    
    private String encrypt(String algorithm, String input) throws Exception {
        switch (algorithm) {
            case "Caesar Cipher":
                return CaesarCipher.encrypt(input, 3);
            case "Affine Cipher":
                return AffineCipher.encrypt(input, 5, 7);
            case "Vigenère Cipher":
                return VigenereCipher.encrypt(input, "KEY");
            case "Playfair Cipher":
                return PlayfairCipher.encrypt(input, "KEYWORD");
            case "Rail Fence Cipher":
                return RailFenceCipher.encrypt(input, 3);
            case "Columnar Transposition":
                return ColumnarTranspositionCipher.encrypt(input, "KEY");
            case "AES-256":
                return AESCipher.encrypt(input, aesKey);
            case "RSA-2048":
                // RSA can only encrypt short messages
                String shortInput = input.length() > 100 ? input.substring(0, 100) : input;
                return RSACipher.encrypt(shortInput, rsaPublicKey);
            case "SHA-256":
                return SHA256Hash.hash(input);
            default:
                throw new IllegalArgumentException("Unknown algorithm");
        }
    }
    
    private String decrypt(String algorithm, String ciphertext) throws Exception {
        switch (algorithm) {
            case "Caesar Cipher":
                return CaesarCipher.decrypt(ciphertext, 3);
            case "Affine Cipher":
                return AffineCipher.decrypt(ciphertext, 5, 7);
            case "Vigenère Cipher":
                return VigenereCipher.decrypt(ciphertext, "KEY");
            case "Playfair Cipher":
                return PlayfairCipher.decrypt(ciphertext, "KEYWORD");
            case "Rail Fence Cipher":
                return RailFenceCipher.decrypt(ciphertext, 3);
            case "Columnar Transposition":
                return ColumnarTranspositionCipher.decrypt(ciphertext, "KEY");
            case "AES-256":
                return AESCipher.decrypt(ciphertext, aesKey);
            case "RSA-2048":
                return RSACipher.decrypt(ciphertext, rsaPrivateKey);
            default:
                throw new IllegalArgumentException("Unknown algorithm");
        }
    }
    
    private void displayResult(BenchmarkResult result) {
        Object[][] data = {{result.algorithm, 
            String.format("%.4f", result.encryptTime), 
            result.decryptTime >= 0 ? String.format("%.4f", result.decryptTime) : "N/A",
            String.format("%.4f", (result.encryptTime + Math.max(0, result.decryptTime)) / 2)}};
        
        resultsTable.setModel(new javax.swing.table.DefaultTableModel(data, 
            new String[]{"Algorithm", "Encryption Time (ms)", "Decryption Time (ms)", "Avg Time (ms)"}));
        
        updateChart(new BenchmarkResult[]{result});
    }
    
    private void displayAllResults(BenchmarkResult[] results) {
        Object[][] data = new Object[results.length][4];
        for (int i = 0; i < results.length; i++) {
            data[i][0] = results[i].algorithm;
            data[i][1] = String.format("%.4f", results[i].encryptTime);
            data[i][2] = results[i].decryptTime >= 0 ? String.format("%.4f", results[i].decryptTime) : "N/A";
            data[i][3] = String.format("%.4f", (results[i].encryptTime + Math.max(0, results[i].decryptTime)) / 2);
        }
        
        resultsTable.setModel(new javax.swing.table.DefaultTableModel(data, 
            new String[]{"Algorithm", "Encryption Time (ms)", "Decryption Time (ms)", "Avg Time (ms)"}));
        
        updateChart(results);
    }
    
    private void updateChart(BenchmarkResult[] results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (BenchmarkResult result : results) {
            dataset.addValue(result.encryptTime, "Encryption", result.algorithm);
            if (result.decryptTime >= 0) {
                dataset.addValue(result.decryptTime, "Decryption", result.algorithm);
            }
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Algorithm Performance Comparison",
            "Algorithm",
            "Time (ms)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Customize chart colors for dark theme
        chart.setBackgroundPaint(DARK_BG);
        chart.getPlot().setBackgroundPaint(DARKER_BG);
        chart.getTitle().setPaint(TEXT_COLOR);
        chart.getLegend().setBackgroundPaint(DARKER_BG);
        chart.getLegend().setItemPaint(TEXT_COLOR);
        chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(TEXT_COLOR);
        chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(TEXT_COLOR);
        chart.getCategoryPlot().getDomainAxis().setLabelPaint(TEXT_COLOR);
        chart.getCategoryPlot().getRangeAxis().setLabelPaint(TEXT_COLOR);
        
        chartPanel.setChart(chart);
    }
    
    private static class BenchmarkResult {
        String algorithm;
        double encryptTime;
        double decryptTime;
        
        BenchmarkResult(String algorithm, double encryptTime, double decryptTime) {
            this.algorithm = algorithm;
            this.encryptTime = encryptTime;
            this.decryptTime = decryptTime;
        }
    }
}
