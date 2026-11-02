"""
Statistical Analysis UI
"""

import customtkinter as ctk
from tkinter import messagebox
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from analysis.frequency import (
    character_frequency, letter_frequency, letter_frequency_percentage,
    compare_frequencies, most_frequent_letters, get_english_frequency_reference
)


class AnalysisFrame(ctk.CTkScrollableFrame):
    """Frame for statistical analysis"""
    
    def __init__(self, master):
        super().__init__(master)
        
        # Title
        title = ctk.CTkLabel(
            self,
            text="Statistical Analysis",
            font=("Roboto", 24, "bold")
        )
        title.pack(pady=(20, 20))
        
        # Input section
        input_frame = ctk.CTkFrame(self)
        input_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(input_frame, text="Input Text:").pack(anchor="w", padx=10)
        self.input_text = ctk.CTkTextbox(input_frame, height=100)
        self.input_text.pack(fill="x", padx=10, pady=5)
        self.input_text.insert("1.0", "The quick brown fox jumps over the lazy dog. This is a sample text for statistical analysis.")
        
        # Controls
        control_frame = ctk.CTkFrame(self)
        control_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(control_frame, text="Analysis Type:").pack(side="left", padx=10)
        
        self.analysis_type_var = ctk.StringVar(value="Letter Frequency (A-Z)")
        self.analysis_combo = ctk.CTkOptionMenu(
            control_frame,
            variable=self.analysis_type_var,
            values=[
                "Character Frequency",
                "Letter Frequency (A-Z)",
                "Comparison: Plaintext vs Ciphertext"
            ]
        )
        self.analysis_combo.pack(side="left", padx=10)
        
        ctk.CTkButton(
            control_frame,
            text="Analyze",
            command=self.perform_analysis,
            width=120
        ).pack(side="left", padx=10)
        
        ctk.CTkButton(
            control_frame,
            text="Compare",
            command=self.perform_comparison,
            fg_color="#2d8a4e",
            hover_color="#3da85e",
            width=120
        ).pack(side="left", padx=10)
        
        # Chart and stats area
        content_frame = ctk.CTkFrame(self)
        content_frame.pack(fill="both", expand=True, pady=20)
        
        # Chart frame
        self.chart_frame = ctk.CTkFrame(content_frame)
        self.chart_frame.pack(side="left", fill="both", expand=True, padx=10)
        
        # Stats frame
        stats_frame = ctk.CTkFrame(content_frame, width=300)
        stats_frame.pack(side="right", fill="y", padx=10)
        stats_frame.pack_propagate(False)
        
        ctk.CTkLabel(stats_frame, text="Statistical Information", font=("Roboto", 14, "bold")).pack(pady=10)
        
        self.stats_text = ctk.CTkTextbox(stats_frame)
        self.stats_text.pack(fill="both", expand=True, padx=10, pady=10)
    
    def perform_analysis(self):
        """Perform selected analysis"""
        analysis_type = self.analysis_type_var.get()
        text = self.input_text.get("1.0", "end-1c")
        
        if not text:
            messagebox.showerror("Error", "Please enter text for analysis.")
            return
        
        if analysis_type == "Character Frequency":
            self.analyze_character_frequency(text)
        elif analysis_type == "Letter Frequency (A-Z)":
            self.analyze_letter_frequency(text)
        elif analysis_type == "Comparison: Plaintext vs Ciphertext":
            messagebox.showinfo("Info", "Use the 'Compare' button for plaintext vs ciphertext comparison.")
    
    def perform_comparison(self):
        """Compare plaintext vs ciphertext frequencies"""
        plaintext = messagebox.askstring("Plaintext", "Enter plaintext:")
        if not plaintext:
            return
        
        ciphertext = messagebox.askstring("Ciphertext", "Enter ciphertext:")
        if not ciphertext:
            return
        
        self.compare_frequencies(plaintext, ciphertext)
    
    def analyze_character_frequency(self, text):
        """Analyze character frequency"""
        freq = character_frequency(text)
        total = len(text)
        
        # Sort by frequency and take top 20
        sorted_freq = sorted(freq.items(), key=lambda x: x[1], reverse=True)[:20]
        
        # Create chart
        self.create_bar_chart(
            [c if c != ' ' else 'Space' if c != '\n' else 'Newline' for c, _ in sorted_freq],
            [count for _, count in sorted_freq],
            "Character Frequency Distribution",
            "Character",
            "Count"
        )
        
        # Update stats
        stats = f"Total Characters: {total}\n"
        stats += f"Unique Characters: {len(freq)}\n\n"
        stats += "Top 10 Characters:\n"
        
        for char, count in sorted_freq[:10]:
            label = 'Space' if char == ' ' else 'Newline' if char == '\n' else char
            pct = (count / total) * 100
            stats += f"  '{label}': {count} ({pct:.2f}%)\n"
        
        self.stats_text.delete("1.0", "end")
        self.stats_text.insert("1.0", stats)
    
    def analyze_letter_frequency(self, text):
        """Analyze letter frequency (A-Z)"""
        freq = letter_frequency(text)
        total = sum(freq.values())
        
        # Create chart
        letters = [chr(ord('A') + i) for i in range(26)]
        counts = [freq.get(letter, 0) for letter in letters]
        
        self.create_bar_chart(
            letters,
            counts,
            "Letter Frequency Distribution (A-Z)",
            "Letter",
            "Count"
        )
        
        # Update stats
        stats = f"Total Letters: {total}\n"
        stats += f"Unique Letters: {len(freq)}\n\n"
        stats += "Letter Frequencies:\n"
        
        for letter in sorted(freq.keys()):
            pct = (freq[letter] / total) * 100
            stats += f"  {letter}: {freq[letter]} ({pct:.2f}%)\n"
        
        # English reference
        stats += "\nEnglish Letter Frequency Reference:\n"
        stats += "  E: 12.7%, T: 9.1%, A: 8.2%, O: 7.5%, I: 7.0%\n"
        stats += "  N: 6.7%, S: 6.3%, H: 6.1%, R: 6.0%, D: 4.3%\n"
        
        self.stats_text.delete("1.0", "end")
        self.stats_text.insert("1.0", stats)
    
    def compare_frequencies(self, plaintext, ciphertext):
        """Compare plaintext and ciphertext frequencies"""
        comparison = compare_frequencies(plaintext, ciphertext)
        
        # Create chart
        letters = [chr(ord('A') + i) for i in range(26)]
        plain_pcts = [comparison[letter]['plaintext_pct'] for letter in letters]
        cipher_pcts = [comparison[letter]['ciphertext_pct'] for letter in letters]
        
        self.create_grouped_bar_chart(
            letters,
            plain_pcts,
            cipher_pcts,
            "Plaintext vs Ciphertext Letter Frequency",
            "Letter",
            "Percentage"
        )
        
        # Update stats
        stats = "Frequency Comparison:\n"
        for letter in letters:
            diff = comparison[letter]['difference']
            stats += f"  {letter}: {comparison[letter]['plaintext_pct']:.2f}% vs {comparison[letter]['ciphertext_pct']:.2f}% (diff: {diff:.2f}%)\n"
        
        stats += "\nAnalysis:\n"
        stats += "  Substitution ciphers preserve letter frequencies.\n"
        stats += "  Transposition ciphers preserve letter frequencies.\n"
        stats += "  Polyalphabetic ciphers flatten letter frequencies.\n"
        
        self.stats_text.delete("1.0", "end")
        self.stats_text.insert("1.0", stats)
    
    def create_bar_chart(self, labels, values, title, xlabel, ylabel):
        """Create a bar chart"""
        # Clear previous chart
        for widget in self.chart_frame.winfo_children():
            widget.destroy()
        
        fig, ax = plt.subplots(figsize=(8, 5))
        ax.bar(labels, values, color='#1f6aa5')
        ax.set_xlabel(xlabel)
        ax.set_ylabel(ylabel)
        ax.set_title(title)
        ax.tick_params(axis='x', rotation=45)
        
        fig.tight_layout()
        
        canvas = FigureCanvasTkAgg(fig, master=self.chart_frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)
    
    def create_grouped_bar_chart(self, labels, values1, values2, title, xlabel, ylabel):
        """Create a grouped bar chart"""
        # Clear previous chart
        for widget in self.chart_frame.winfo_children():
            widget.destroy()
        
        fig, ax = plt.subplots(figsize=(8, 5))
        
        x = range(len(labels))
        width = 0.35
        
        ax.bar([i - width/2 for i in x], values1, width, label='Plaintext', color='#1f6aa5')
        ax.bar([i + width/2 for i in x], values2, width, label='Ciphertext', color='#2d8a4e')
        
        ax.set_xlabel(xlabel)
        ax.set_ylabel(ylabel)
        ax.set_title(title)
        ax.set_xticks(x)
        ax.set_xticklabels(labels)
        ax.legend()
        
        fig.tight_layout()
        
        canvas = FigureCanvasTkAgg(fig, master=self.chart_frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)
