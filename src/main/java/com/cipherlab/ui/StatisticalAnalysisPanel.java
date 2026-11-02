package com.cipherlab.ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Module 3: Statistical Analysis
 */
public class StatisticalAnalysisPanel extends JPanel {
    
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(25, 25, 25);
    private static final Color ACCENT_COLOR = new Color(0, 150, 200);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    private JTextArea inputTextArea;
    private JComboBox<String> analysisTypeCombo;
    private ChartPanel frequencyChartPanel;
    private JTextArea statsTextArea;
    
    public StatisticalAnalysisPanel() {
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
        
        // Right panel: Charts and statistics
        JPanel rightPanel = createRightPanel();
        splitPane.setRightComponent(rightPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Statistical Analysis");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Input section
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBackground(DARK_BG);
        
        JLabel inputLabel = new JLabel("Input Text for Analysis:");
        inputLabel.setForeground(TEXT_COLOR);
        inputLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        inputTextArea = new JTextArea(15, 30);
        inputTextArea.setBackground(DARKER_BG);
        inputTextArea.setForeground(TEXT_COLOR);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setText("The quick brown fox jumps over the lazy dog. " +
            "This is a sample text for statistical analysis. " +
            "Frequency analysis helps identify patterns in encrypted text.");
        
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        inputScroll.setBackground(DARKER_BG);
        
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputScroll, BorderLayout.CENTER);
        
        // Controls section
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        controlsPanel.setBackground(DARK_BG);
        
        JLabel typeLabel = new JLabel("Analysis Type:");
        typeLabel.setForeground(TEXT_COLOR);
        
        analysisTypeCombo = new JComboBox<>(new String[]{
            "Character Frequency",
            "Letter Frequency (A-Z)",
            "Comparison: Plaintext vs Ciphertext"
        });
        analysisTypeCombo.setBackground(DARKER_BG);
        analysisTypeCombo.setForeground(TEXT_COLOR);
        
        JButton analyzeBtn = new JButton("Analyze");
        analyzeBtn.setBackground(ACCENT_COLOR);
        analyzeBtn.setForeground(Color.WHITE);
        analyzeBtn.setPreferredSize(new Dimension(120, 40));
        analyzeBtn.addActionListener(e -> performAnalysis());
        
        JButton compareBtn = new JButton("Compare");
        compareBtn.setBackground(new Color(100, 150, 100));
        compareBtn.setForeground(Color.WHITE);
        compareBtn.setPreferredSize(new Dimension(120, 40));
        compareBtn.addActionListener(e -> performComparison());
        
        controlsPanel.add(typeLabel);
        controlsPanel.add(analysisTypeCombo);
        controlsPanel.add(analyzeBtn);
        controlsPanel.add(compareBtn);
        
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(controlsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Chart panel
        JPanel chartContainer = new JPanel(new BorderLayout());
        chartContainer.setBackground(DARK_BG);
        chartContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            "Frequency Distribution",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14),
            TEXT_COLOR
        ));
        
        frequencyChartPanel = new ChartPanel(null);
        chartContainer.add(frequencyChartPanel, BorderLayout.CENTER);
        
        // Statistics text area
        JPanel statsContainer = new JPanel(new BorderLayout());
        statsContainer.setBackground(DARK_BG);
        statsContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            "Statistical Information",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 14),
            TEXT_COLOR
        ));
        
        statsTextArea = new JTextArea(10, 40);
        statsTextArea.setBackground(DARKER_BG);
        statsTextArea.setForeground(TEXT_COLOR);
        statsTextArea.setEditable(false);
        statsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane statsScroll = new JScrollPane(statsTextArea);
        statsScroll.setBackground(DARKER_BG);
        
        statsContainer.add(statsScroll, BorderLayout.CENTER);
        
        panel.add(chartContainer, BorderLayout.CENTER);
        panel.add(statsContainer, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void performAnalysis() {
        String input = inputTextArea.getText();
        
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter text for analysis.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String analysisType = (String) analysisTypeCombo.getSelectedItem();
        
        switch (analysisType) {
            case "Character Frequency":
                analyzeCharacterFrequency(input);
                break;
            case "Letter Frequency (A-Z)":
                analyzeLetterFrequency(input);
                break;
            case "Comparison: Plaintext vs Ciphertext":
                JOptionPane.showMessageDialog(this, 
                    "Use the 'Compare' button for plaintext vs ciphertext comparison.", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
    
    private void performComparison() {
        String plaintext = JOptionPane.showInputDialog(this, 
            "Enter plaintext:", "Plaintext", JOptionPane.QUESTION_MESSAGE);
        
        if (plaintext == null || plaintext.isEmpty()) {
            return;
        }
        
        String ciphertext = JOptionPane.showInputDialog(this, 
            "Enter ciphertext:", "Ciphertext", JOptionPane.QUESTION_MESSAGE);
        
        if (ciphertext == null || ciphertext.isEmpty()) {
            return;
        }
        
        compareFrequencies(plaintext, ciphertext);
    }
    
    private void analyzeCharacterFrequency(String text) {
        Map<Character, Integer> frequency = new HashMap<>();
        int totalChars = 0;
        
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
            totalChars++;
        }
        
        // Create chart dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Sort by frequency and take top 20
        frequency.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(20)
            .forEach(entry -> {
                char c = entry.getKey();
                String label = Character.isWhitespace(c) ? "Space" : 
                    (c == '\n' ? "Newline" : String.valueOf(c));
                dataset.addValue(entry.getValue(), "Frequency", label);
            });
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Character Frequency Distribution",
            "Character",
            "Count",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        customizeChart(chart);
        frequencyChartPanel.setChart(chart);
        
        // Update statistics
        StringBuilder stats = new StringBuilder();
        stats.append("Total Characters: ").append(totalChars).append("\n");
        stats.append("Unique Characters: ").append(frequency.size()).append("\n\n");
        stats.append("Top 10 Characters:\n");
        
        frequency.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .limit(10)
            .forEach(entry -> {
                char c = entry.getKey();
                String label = Character.isWhitespace(c) ? "Space" : 
                    (c == '\n' ? "Newline" : String.valueOf(c));
                double percentage = (entry.getValue() * 100.0) / totalChars;
                stats.append(String.format("  '%s': %d (%.2f%%)\n", 
                    label, entry.getValue(), percentage));
            });
        
        statsTextArea.setText(stats.toString());
    }
    
    private void analyzeLetterFrequency(String text) {
        int[] letterCounts = new int[26];
        int totalLetters = 0;
        
        for (char c : text.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                letterCounts[c - 'A']++;
                totalLetters++;
            }
        }
        
        // Create chart dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i = 0; i < 26; i++) {
            dataset.addValue(letterCounts[i], "Count", String.valueOf((char) ('A' + i)));
        }
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Letter Frequency Distribution (A-Z)",
            "Letter",
            "Count",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        customizeChart(chart);
        frequencyChartPanel.setChart(chart);
        
        // Update statistics
        StringBuilder stats = new StringBuilder();
        stats.append("Total Letters: ").append(totalLetters).append("\n");
        stats.append("Unique Letters: ").append(countUniqueLetters(letterCounts)).append("\n\n");
        stats.append("Letter Frequencies:\n");
        
        for (int i = 0; i < 26; i++) {
            if (letterCounts[i] > 0) {
                double percentage = (letterCounts[i] * 100.0) / totalLetters;
                stats.append(String.format("  %c: %d (%.2f%%)\n", 
                    (char) ('A' + i), letterCounts[i], percentage));
            }
        }
        
        // English letter frequency reference
        stats.append("\nEnglish Letter Frequency Reference:\n");
        stats.append("  E: 12.7%, T: 9.1%, A: 8.2%, O: 7.5%, I: 7.0%\n");
        stats.append("  N: 6.7%, S: 6.3%, H: 6.1%, R: 6.0%, D: 4.3%\n");
        
        statsTextArea.setText(stats.toString());
    }
    
    private void compareFrequencies(String plaintext, String ciphertext) {
        int[] plainCounts = new int[26];
        int[] cipherCounts = new int[26];
        int plainTotal = 0;
        int cipherTotal = 0;
        
        for (char c : plaintext.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                plainCounts[c - 'A']++;
                plainTotal++;
            }
        }
        
        for (char c : ciphertext.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                cipherCounts[c - 'A']++;
                cipherTotal++;
            }
        }
        
        // Create chart dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        for (int i = 0; i < 26; i++) {
            dataset.addValue(plainCounts[i], "Plaintext", String.valueOf((char) ('A' + i)));
            dataset.addValue(cipherCounts[i], "Ciphertext", String.valueOf((char) ('A' + i)));
        }
        
        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
            "Plaintext vs Ciphertext Letter Frequency",
            "Letter",
            "Count",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        customizeChart(chart);
        frequencyChartPanel.setChart(chart);
        
        // Update statistics
        StringBuilder stats = new StringBuilder();
        stats.append("Plaintext Total Letters: ").append(plainTotal).append("\n");
        stats.append("Ciphertext Total Letters: ").append(cipherTotal).append("\n\n");
        stats.append("Frequency Comparison:\n");
        
        for (int i = 0; i < 26; i++) {
            char letter = (char) ('A' + i);
            double plainPct = plainTotal > 0 ? (plainCounts[i] * 100.0) / plainTotal : 0;
            double cipherPct = cipherTotal > 0 ? (cipherCounts[i] * 100.0) / cipherTotal : 0;
            double diff = Math.abs(plainPct - cipherPct);
            
            stats.append(String.format("  %c: %.2f%% vs %.2f%% (diff: %.2f%%)\n", 
                letter, plainPct, cipherPct, diff));
        }
        
        stats.append("\nAnalysis:\n");
        stats.append("  Substitution ciphers preserve letter frequencies.\n");
        stats.append("  Transposition ciphers preserve letter frequencies.\n");
        stats.append("  Polyalphabetic ciphers flatten letter frequencies.\n");
        
        statsTextArea.setText(stats.toString());
    }
    
    private int countUniqueLetters(int[] counts) {
        int count = 0;
        for (int c : counts) {
            if (c > 0) count++;
        }
        return count;
    }
    
    private void customizeChart(JFreeChart chart) {
        chart.setBackgroundPaint(DARK_BG);
        chart.getPlot().setBackgroundPaint(DARKER_BG);
        chart.getTitle().setPaint(TEXT_COLOR);
        chart.getLegend().setBackgroundPaint(DARKER_BG);
        chart.getLegend().setItemPaint(TEXT_COLOR);
        chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(TEXT_COLOR);
        chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(TEXT_COLOR);
        chart.getCategoryPlot().getDomainAxis().setLabelPaint(TEXT_COLOR);
        chart.getCategoryPlot().getRangeAxis().setLabelPaint(TEXT_COLOR);
    }
}
