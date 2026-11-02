"""
Cryptanalysis UI
"""

import customtkinter as ctk
from tkinter import messagebox
from cryptanalysis.brute_force import format_caesar_results, format_affine_results
from cryptanalysis.frequency_attack import frequency_analysis
from cryptanalysis.known_plaintext import known_plaintext_attack


class CryptanalysisFrame(ctk.CTkScrollableFrame):
    """Frame for cryptanalysis attacks"""
    
    def __init__(self, master):
        super().__init__(master)
        
        # Title
        title = ctk.CTkLabel(
            self,
            text="Cryptanalysis & Vulnerability Testing",
            font=("Roboto", 24, "bold")
        )
        title.pack(pady=(20, 20))
        
        # Attack selection
        attack_frame = ctk.CTkFrame(self)
        attack_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(attack_frame, text="Attack Type:").pack(side="left", padx=10)
        
        self.attack_type_var = ctk.StringVar(value="Caesar Brute-Force")
        self.attack_combo = ctk.CTkOptionMenu(
            attack_frame,
            variable=self.attack_type_var,
            values=[
                "Caesar Brute-Force",
                "Affine Brute-Force",
                "Frequency Analysis",
                "Known-Plaintext Attack"
            ],
            command=self.update_input_fields
        )
        self.attack_combo.pack(side="left", padx=10)
        
        # Ciphertext input
        ctk.CTkLabel(self, text="Ciphertext to Attack:").pack(anchor="w", padx=10)
        self.ciphertext_text = ctk.CTkTextbox(self, height=100)
        self.ciphertext_text.pack(fill="x", padx=10, pady=5)
        
        # Known plaintext/ciphertext fields
        self.known_frame = ctk.CTkFrame(self)
        self.known_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(self.known_frame, text="Known Plaintext:").grid(row=0, column=0, padx=10, pady=10)
        self.known_plain_entry = ctk.CTkEntry(self.known_frame, width=300)
        self.known_plain_entry.grid(row=0, column=1, padx=10, pady=10)
        
        ctk.CTkLabel(self.known_frame, text="Known Ciphertext:").grid(row=0, column=2, padx=10, pady=10)
        self.known_cipher_entry = ctk.CTkEntry(self.known_frame, width=300)
        self.known_cipher_entry.grid(row=0, column=3, padx=10, pady=10)
        
        # Buttons
        button_frame = ctk.CTkFrame(self)
        button_frame.pack(fill="x", pady=10)
        
        ctk.CTkButton(
            button_frame,
            text="Launch Attack",
            command=self.perform_attack,
            width=150
        ).pack(side="left", padx=10)
        
        ctk.CTkButton(
            button_frame,
            text="Clear",
            command=self.clear_fields,
            fg_color="darkred",
            hover_color="red",
            width=100
        ).pack(side="left", padx=10)
        
        # Results
        ctk.CTkLabel(self, text="Attack Results:").pack(anchor="w", padx=10, pady=(10, 0))
        self.results_text = ctk.CTkTextbox(self, height=300)
        self.results_text.pack(fill="both", expand=True, padx=10, pady=5)
        
        # Initialize
        self.update_input_fields("Caesar Brute-Force")
    
    def update_input_fields(self, attack_type):
        """Update input fields based on attack type"""
        attack_type = self.attack_type_var.get()
        
        show_known_fields = attack_type == "Known-Plaintext Attack"
        
        if show_known_fields:
            self.known_frame.pack(fill="x", pady=10)
        else:
            self.known_frame.pack_forget()
    
    def perform_attack(self):
        """Perform selected attack"""
        attack_type = self.attack_type_var.get()
        ciphertext = self.ciphertext_text.get("1.0", "end-1c")
        
        if not ciphertext:
            messagebox.showerror("Error", "Please enter ciphertext to attack.")
            return
        
        self.results_text.delete("1.0", "end")
        
        if attack_type == "Caesar Brute-Force":
            results = format_caesar_results(ciphertext)
            self.results_text.insert("1.0", results)
        elif attack_type == "Affine Brute-Force":
            results = format_affine_results(ciphertext)
            self.results_text.insert("1.0", results)
        elif attack_type == "Frequency Analysis":
            results = frequency_analysis(ciphertext)
            self.results_text.insert("1.0", results)
        elif attack_type == "Known-Plaintext Attack":
            known_plain = self.known_plain_entry.get()
            known_cipher = self.known_cipher_entry.get()
            
            if not known_plain or not known_cipher:
                messagebox.showerror("Error", "Please enter both known plaintext and ciphertext.")
                return
            
            results = known_plaintext_attack(known_plain, known_cipher, ciphertext)
            self.results_text.insert("1.0", results)
    
    def clear_fields(self):
        """Clear all fields"""
        self.ciphertext_text.delete("1.0", "end")
        self.known_plain_entry.delete(0, "end")
        self.known_cipher_entry.delete(0, "end")
        self.results_text.delete("1.0", "end")
