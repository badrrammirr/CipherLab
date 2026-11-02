"""
Dashboard UI module - Main application window with sidebar navigation
"""

import customtkinter as ctk
from tkinter import ttk, messagebox
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
import pandas as pd


class CipherLabApp(ctk.CTk):
    """Main application class for CipherLab"""
    
    def __init__(self):
        super().__init__()
        
        # Configure window
        self.title("CipherLab - Cryptography Benchmarking & Security Analysis Platform")
        self.geometry("1400x900")
        
        # Set theme
        ctk.set_appearance_mode("dark")
        ctk.set_default_color_theme("dark-blue")
        
        # Initialize variables
        self.current_frame = None
        self.aes_key = None
        self.rsa_public_key = None
        self.rsa_private_key = None
        
        # Create UI
        self.create_sidebar()
        self.create_content_area()
        
        # Show default frame
        self.show_frame("dashboard")
    
    def create_sidebar(self):
        """Create the sidebar navigation"""
        self.sidebar = ctk.CTkFrame(self, width=250, corner_radius=0)
        self.sidebar.pack(side="left", fill="y")
        self.sidebar.pack_propagate(False)
        
        # Title
        title_label = ctk.CTkLabel(
            self.sidebar, 
            text="CipherLab", 
            font=("Roboto", 24, "bold"),
            text_color="#1f6aa5"
        )
        title_label.pack(pady=(30, 20))
        
        # Navigation buttons
        self.nav_buttons = {}
        nav_items = [
            ("Dashboard", "dashboard"),
            ("Encryption Lab", "encryption"),
            ("Performance", "benchmark"),
            ("Statistics", "analysis"),
            ("Cryptanalysis", "cryptanalysis"),
            ("Comparison", "comparison"),
            ("About", "about")
        ]
        
        for text, frame_id in nav_items:
            btn = ctk.CTkButton(
                self.sidebar,
                text=text,
                font=("Roboto", 14),
                height=50,
                fg_color="transparent",
                text_color=("gray10", "gray90"),
                hover_color=("gray70", "gray30"),
                anchor="w",
                command=lambda f=frame_id: self.show_frame(f)
            )
            btn.pack(side="top", fill="x", padx=20, pady=5)
            self.nav_buttons[frame_id] = btn
    
    def create_content_area(self):
        """Create the main content area"""
        self.content_area = ctk.CTkFrame(self)
        self.content_area.pack(side="right", fill="both", expand=True)
        
        # Create frame containers
        self.frames = {}
        
        # Dashboard frame
        self.frames["dashboard"] = self.create_dashboard_frame()
        self.frames["encryption"] = self.create_encryption_frame()
        self.frames["benchmark"] = self.create_benchmark_frame()
        self.frames["analysis"] = self.create_analysis_frame()
        self.frames["cryptanalysis"] = self.create_cryptanalysis_frame()
        self.frames["comparison"] = self.create_comparison_frame()
        self.frames["about"] = self.create_about_frame()
    
    def show_frame(self, frame_id):
        """Show the specified frame"""
        # Hide current frame
        if self.current_frame:
            self.current_frame.pack_forget()
        
        # Show new frame
        self.current_frame = self.frames[frame_id]
        self.current_frame.pack(fill="both", expand=True, padx=20, pady=20)
        
        # Update button states
        for fid, btn in self.nav_buttons.items():
            if fid == frame_id:
                btn.configure(fg_color="#1f6aa5", text_color="white")
            else:
                btn.configure(fg_color="transparent", text_color=("gray10", "gray90"))
    
    def create_dashboard_frame(self):
        """Create the dashboard frame"""
        frame = ctk.CTkScrollableFrame(self.content_area)
        
        title = ctk.CTkLabel(
            frame, 
            text="Welcome to CipherLab",
            font=("Roboto", 28, "bold")
        )
        title.pack(pady=(20, 10))
        
        subtitle = ctk.CTkLabel(
            frame,
            text="Cryptography Benchmarking & Security Analysis Platform",
            font=("Roboto", 16),
            text_color="gray"
        )
        subtitle.pack(pady=(0, 30))
        
        # Feature cards
        features_frame = ctk.CTkFrame(frame)
        features_frame.pack(fill="x", pady=10)
        
        features = [
            ("Encryption Lab", "Encrypt and decrypt with 9 different algorithms"),
            ("Performance", "Benchmark and compare algorithm performance"),
            ("Statistics", "Analyze character frequency distributions"),
            ("Cryptanalysis", "Test vulnerabilities with attack simulations"),
            ("Comparison", "Compare security characteristics")
        ]
        
        for title, desc in features:
            card = ctk.CTkFrame(features_frame)
            card.pack(fill="x", padx=10, pady=5)
            
            card_title = ctk.CTkLabel(card, text=title, font=("Roboto", 16, "bold"))
            card_title.pack(anchor="w", padx=10, pady=(10, 5))
            
            card_desc = ctk.CTkLabel(card, text=desc, font=("Roboto", 12), text_color="gray")
            card_desc.pack(anchor="w", padx=10, pady=(0, 10))
        
        return frame
    
    def create_encryption_frame(self):
        """Create the encryption/decryption laboratory frame"""
        from ui.encryption import EncryptionFrame
        return EncryptionFrame(self.content_area, self)
    
    def create_benchmark_frame(self):
        """Create the performance benchmark frame"""
        from ui.benchmark import BenchmarkFrame
        return BenchmarkFrame(self.content_area, self)
    
    def create_analysis_frame(self):
        """Create the statistical analysis frame"""
        from ui.analysis import AnalysisFrame
        return AnalysisFrame(self.content_area)
    
    def create_cryptanalysis_frame(self):
        """Create the cryptanalysis frame"""
        from ui.cryptanalysis import CryptanalysisFrame
        return CryptanalysisFrame(self.content_area)
    
    def create_comparison_frame(self):
        """Create the comparison dashboard frame"""
        from ui.comparison import ComparisonFrame
        return ComparisonFrame(self.content_area)
    
    def create_about_frame(self):
        """Create the about frame"""
        frame = ctk.CTkScrollableFrame(self.content_area)
        
        title = ctk.CTkLabel(
            frame,
            text="About CipherLab",
            font=("Roboto", 28, "bold")
        )
        title.pack(pady=(20, 10))
        
        about_text = """
CipherLab is an educational cryptography platform designed to implement, 
analyze, benchmark, and compare classical and modern cryptographic algorithms.

Features:
• 9 Cryptographic Algorithms (6 classical, 3 modern)
• Performance Benchmarking
• Statistical Analysis
• Cryptanalysis Attacks
• Comprehensive Comparison Dashboard

Technology Stack:
• Python 3.11+
• CustomTkinter for UI
• PyCryptodome for modern cryptography
• Matplotlib for visualizations
• Pandas for data analysis

Educational Purpose:
This application is designed for educational purposes to demonstrate how different 
encryption algorithms work, their performance characteristics, statistical properties, 
and vulnerabilities.

Security Note:
Classical ciphers are NOT secure for modern applications. They are included here 
for educational purposes only. Modern ciphers (AES, RSA, SHA-256) are secure when 
used correctly.
        """
        
        about_label = ctk.CTkLabel(
            frame,
            text=about_text,
            font=("Roboto", 12),
            justify="left"
        )
        about_label.pack(pady=20, padx=20, anchor="w")
        
        return frame
