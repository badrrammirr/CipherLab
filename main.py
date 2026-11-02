"""
CipherLab - Cryptography Benchmarking & Security Analysis Platform
Main entry point for the application
"""

import sys
import os

# Add project root to path
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

import customtkinter as ctk
from ui.dashboard import CipherLabApp


def main():
    """Main function to run the application"""
    # Set appearance mode
    ctk.set_appearance_mode("dark")
    ctk.set_default_color_theme("dark-blue")
    
    # Create and run application
    app = CipherLabApp()
    app.mainloop()


if __name__ == "__main__":
    main()
