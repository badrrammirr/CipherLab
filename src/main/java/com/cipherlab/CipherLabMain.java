package com.cipherlab;

import com.cipherlab.ui.MainFrame;

import javax.swing.*;

/**
 * Main entry point for CipherLab application
 */
public class CipherLabMain {
    public static void main(String[] args) {
        try {
            // Set system look and feel for better native appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
