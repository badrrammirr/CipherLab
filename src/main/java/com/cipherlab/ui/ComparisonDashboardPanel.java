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

/**
 * Module 5: Comparison Dashboard
 */
public class ComparisonDashboardPanel extends JPanel {
    
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(25, 25, 25);
    private static final Color ACCENT_COLOR = new Color(0, 150, 200);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    private JTable comparisonTable;
    private ChartPanel keySpaceChart;
    private ChartPanel performanceChart;
    private JTextArea infoTextArea;
    
    public ComparisonDashboardPanel() {
        setLayout(new BorderLayout());
        setBackground(DARK_BG);
        initializeUI();
    }
    
    private void initializeUI() {
        // Header
        JPanel headerPanel = createHeader();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setBackground(DARK_BG);
        mainSplit.setDividerLocation(350);
        
        // Top: Comparison table
        JPanel topPanel = createComparisonTablePanel();
        mainSplit.setTopComponent(topPanel);
        
        // Bottom: Charts and info
        JSplitPane bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        bottomSplit.setBackground(DARK_BG);
        bottomSplit.setDividerLocation(500);
        
        JPanel chartsPanel = createChartsPanel();
        JPanel infoPanel = createInfoPanel();
        
        bottomSplit.setLeftComponent(chartsPanel);
        bottomSplit.setRightComponent(infoPanel);
        
        mainSplit.setBottomComponent(bottomSplit);
        
        add(mainSplit, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Algorithm Comparison Dashboard");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        panel.add(titleLabel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel createComparisonTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel tableLabel = new JLabel("Algorithm Comparison Table");
        tableLabel.setForeground(TEXT_COLOR);
        tableLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        String[] columnNames = {
            "Algorithm",
            "Category",
            "Key Space / Size",
            "Speed",
            "Security",
            "Vulnerability",
            "Applications"
        };
        
        Object[][] data = {
            {
                "Caesar Cipher",
                "Classical - Substitution",
                "26 (2^4.7)",
                "Very Fast",
                "Very Low",
                "Brute-force (26 keys)",
                "Educational, simple puzzles"
            },
            {
                "Affine Cipher",
                "Classical - Substitution",
                "312 (2^8.3)",
                "Very Fast",
                "Low",
                "Brute-force (312 keys)",
                "Educational"
            },
            {
                "Vigenère Cipher",
                "Classical - Polyalphabetic",
                "26^k (k=key length)",
                "Fast",
                "Low-Medium",
                "Kasiski, Friedman",
                "Historical, educational"
            },
            {
                "Playfair Cipher",
                "Classical - Digraph",
                "25! (~2^84)",
                "Fast",
                "Medium",
                "Digraph analysis",
                "Historical military"
            },
            {
                "Rail Fence",
                "Classical - Transposition",
                "n (message length)",
                "Very Fast",
                "Very Low",
                "Anagram analysis",
                "Educational"
            },
            {
                "Columnar Transposition",
                "Classical - Transposition",
                "k! (k=key length)",
                "Fast",
                "Low",
                "Anagram analysis",
                "Historical"
            },
            {
                "AES-256",
                "Modern - Symmetric",
                "2^256",
                "Fast",
                "Very High",
                "None known (quantum: 2^128)",
                "Government, data encryption"
            },
            {
                "RSA-2048",
                "Modern - Asymmetric",
                "2^2048",
                "Slow",
                "High",
                "Quantum (Shor's algorithm)",
                "Digital signatures, key exchange"
            },
            {
                "SHA-256",
                "Modern - Hash",
                "N/A (one-way)",
                "Very Fast",
                "High (collision resistant)",
                "Collision attacks (theoretical)",
                "Integrity verification, blockchain"
            }
        };
        
        comparisonTable = new JTable(data, columnNames);
        comparisonTable.setBackground(DARKER_BG);
        comparisonTable.setForeground(TEXT_COLOR);
        comparisonTable.getTableHeader().setBackground(new Color(20, 20, 20));
        comparisonTable.getTableHeader().setForeground(TEXT_COLOR);
        comparisonTable.setRowHeight(25);
        comparisonTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set column widths
        comparisonTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        comparisonTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        comparisonTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        comparisonTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        comparisonTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        comparisonTable.getColumnModel().getColumn(5).setPreferredWidth(200);
        comparisonTable.getColumnModel().getColumn(6).setPreferredWidth(200);
        
        JScrollPane tableScroll = new JScrollPane(comparisonTable);
        tableScroll.setBackground(DARKER_BG);
        
        panel.add(tableLabel, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Key space chart
        JPanel keySpacePanel = new JPanel(new BorderLayout());
        keySpacePanel.setBackground(DARK_BG);
        keySpacePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            "Key Space Comparison (Log Scale)",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 12),
            TEXT_COLOR
        ));
        
        keySpaceChart = createKeySpaceChart();
        keySpacePanel.add(keySpaceChart, BorderLayout.CENTER);
        
        // Performance chart
        JPanel performancePanel = new JPanel(new BorderLayout());
        performancePanel.setBackground(DARK_BG);
        performancePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR),
            "Relative Performance",
            0,
            0,
            new Font("Segoe UI", Font.BOLD, 12),
            TEXT_COLOR
        ));
        
        performanceChart = createPerformanceChart();
        performancePanel.add(performanceChart, BorderLayout.CENTER);
        
        panel.add(keySpacePanel);
        panel.add(performancePanel);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel infoLabel = new JLabel("Security Analysis Notes");
        infoLabel.setForeground(TEXT_COLOR);
        infoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        infoTextArea = new JTextArea();
        infoTextArea.setBackground(DARKER_BG);
        infoTextArea.setForeground(TEXT_COLOR);
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoTextArea.setText(
            "CLASSICAL CIPHERS:\n" +
            "-----------------\n" +
            "• All classical ciphers are vulnerable to modern cryptanalysis\n" +
            "• Caesar and Affine: Extremely small key spaces, easily brute-forced\n" +
            "• Vigenère: Vulnerable to Kasiski examination and frequency analysis\n" +
            "• Playfair: Better than simple substitution but still breakable\n" +
            "• Transposition ciphers: Preserve letter frequencies, vulnerable to anagrams\n" +
            "\n" +
            "MODERN CIPHERS:\n" +
            "----------------\n" +
            "• AES-256: Considered secure against all known practical attacks\n" +
            "  - Key space: 2^256 (impossible to brute-force)\n" +
            "  - Used by governments, military, and industry worldwide\n" +
            "\n" +
            "• RSA-2048: Secure against classical computers\n" +
            "  - Security based on integer factorization problem\n" +
            "  - Vulnerable to quantum computers (Shor's algorithm)\n" +
            "  - Used for digital signatures and key exchange\n" +
            "\n" +
            "• SHA-256: Cryptographic hash function\n" +
            "  - One-way function, preimage resistant\n" +
            "  - Collision resistance: 2^128 operations (theoretical)\n" +
            "  - Used for integrity verification and Bitcoin mining\n" +
            "\n" +
            "QUANTUM THREATS:\n" +
            "-----------------\n" +
            "• Grover's algorithm: Reduces security by square root (AES: 2^256 → 2^128)\n" +
            "• Shor's algorithm: Breaks RSA and ECC completely\n" +
            "• Post-quantum cryptography is being developed to address these threats\n"
        );
        
        JScrollPane infoScroll = new JScrollPane(infoTextArea);
        infoScroll.setBackground(DARKER_BG);
        
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(infoScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private ChartPanel createKeySpaceChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Log scale representation (using log2 of key space)
        dataset.addValue(4.7, "Key Space (log2)", "Caesar");
        dataset.addValue(8.3, "Key Space (log2)", "Affine");
        dataset.addValue(26.0, "Key Space (log2)", "Vigenère (k=5)");
        dataset.addValue(84.0, "Key Space (log2)", "Playfair");
        dataset.addValue(256.0, "Key Space (log2)", "AES-256");
        dataset.addValue(2048.0, "Key Space (log2)", "RSA-2048");
        
        JFreeChart chart = ChartFactory.createBarChart(
            "",
            "Algorithm",
            "Key Space (log2 bits)",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        customizeChart(chart);
        return new ChartPanel(chart);
    }
    
    private ChartPanel createPerformanceChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Relative performance (higher = faster)
        dataset.addValue(100, "Speed", "Caesar");
        dataset.addValue(95, "Speed", "Affine");
        dataset.addValue(90, "Speed", "Vigenère");
        dataset.addValue(85, "Speed", "Playfair");
        dataset.addValue(100, "Speed", "Rail Fence");
        dataset.addValue(85, "Speed", "Columnar");
        dataset.addValue(70, "Speed", "AES-256");
        dataset.addValue(20, "Speed", "RSA-2048");
        dataset.addValue(95, "Speed", "SHA-256");
        
        JFreeChart chart = ChartFactory.createBarChart(
            "",
            "Algorithm",
            "Relative Speed",
            dataset,
            PlotOrientation.VERTICAL,
            false,
            true,
            false
        );
        
        customizeChart(chart);
        return new ChartPanel(chart);
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
