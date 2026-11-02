package com.cipherlab.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame with dark theme and sidebar navigation
 */
public class MainFrame extends JFrame {
    
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    
    private JPanel sidebar;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Colors for dark theme
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARKER_BG = new Color(25, 25, 25);
    private static final Color SIDEBAR_BG = new Color(20, 20, 20);
    private static final Color ACCENT_COLOR = new Color(0, 150, 200);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color BUTTON_HOVER = new Color(0, 170, 220);
    
    public MainFrame() {
        setTitle("CipherLab - Cryptography Benchmarking & Security Analysis Platform");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Sidebar
        sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Content panel with card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(DARK_BG);
        add(contentPanel, BorderLayout.CENTER);
        
        // Add panels
        contentPanel.add(new EncryptionLabPanel(), "encryption");
        contentPanel.add(new BenchmarkPanel(), "benchmark");
        contentPanel.add(new StatisticalAnalysisPanel(), "stats");
        contentPanel.add(new CryptanalysisPanel(), "cryptanalysis");
        contentPanel.add(new ComparisonDashboardPanel(), "comparison");
        
        // Apply dark theme to all components
        applyDarkTheme();
    }
    
    private JPanel createSidebar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(250, HEIGHT));
        
        // Title
        JLabel titleLabel = new JLabel("CipherLab");
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Navigation buttons
        JPanel navPanel = new JPanel(new GridLayout(5, 1, 0, 5));
        navPanel.setBackground(SIDEBAR_BG);
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        String[] buttonLabels = {
            "Encryption Lab",
            "Performance Benchmark",
            "Statistical Analysis",
            "Cryptanalysis",
            "Comparison Dashboard"
        };
        
        String[] cardNames = {
            "encryption",
            "benchmark",
            "stats",
            "cryptanalysis",
            "comparison"
        };
        
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createNavButton(buttonLabels[i], cardNames[i]);
            navPanel.add(button);
        }
        
        panel.add(navPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createNavButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setBackground(DARKER_BG);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            updateButtonStates(button);
        });
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(BUTTON_HOVER);
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.isSelected()) {
                    button.setBackground(DARKER_BG);
                }
            }
        });
        
        return button;
    }
    
    private void updateButtonStates(JButton selectedButton) {
        for (Component comp : ((JPanel) sidebar.getComponentAt(1, 1)).getComponents()) {
            if (comp instanceof JButton) {
                comp.setBackground(DARKER_BG);
            }
        }
        selectedButton.setBackground(ACCENT_COLOR);
    }
    
    private void applyDarkTheme() {
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("TextField.background", DARKER_BG);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextArea.background", DARKER_BG);
        UIManager.put("TextArea.foreground", TEXT_COLOR);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.background", DARKER_BG);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("List.background", DARKER_BG);
        UIManager.put("List.foreground", TEXT_COLOR);
        UIManager.put("Table.background", DARKER_BG);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("TableHeader.background", SIDEBAR_BG);
        UIManager.put("TableHeader.foreground", TEXT_COLOR);
        UIManager.put("ScrollPane.background", DARK_BG);
    }
}
