"""
Encryption/Decryption Laboratory UI
"""

import customtkinter as ctk
from tkinter import messagebox
import pyperclip


class EncryptionFrame(ctk.CTkScrollableFrame):
    """Frame for encryption/decryption laboratory"""
    
    def __init__(self, master, app):
        super().__init__(master)
        self.app = app
        
        # Title
        title = ctk.CTkLabel(
            self,
            text="Encryption / Decryption Laboratory",
            font=("Roboto", 24, "bold")
        )
        title.pack(pady=(20, 20))
        
        # Algorithm selection
        algo_frame = ctk.CTkFrame(self)
        algo_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(algo_frame, text="Select Algorithm:").pack(side="left", padx=10)
        
        self.algorithm_var = ctk.StringVar(value="Caesar Cipher")
        self.algorithm_combo = ctk.CTkOptionMenu(
            algo_frame,
            variable=self.algorithm_var,
            values=[
                "Caesar Cipher",
                "Affine Cipher",
                "Vigenère Cipher",
                "Playfair Cipher",
                "Rail Fence Cipher",
                "Columnar Transposition",
                "AES-256",
                "RSA-2048",
                "SHA-256"
            ],
            command=self.update_key_fields
        )
        self.algorithm_combo.pack(side="left", padx=10)
        
        # Key fields
        self.key_frame = ctk.CTkFrame(self)
        self.key_frame.pack(fill="x", pady=10)
        
        self.key1_label = ctk.CTkLabel(self.key_frame, text="Key 1:")
        self.key1_label.grid(row=0, column=0, padx=10, pady=10, sticky="w")
        
        self.key1_entry = ctk.CTkEntry(self.key_frame, width=300)
        self.key1_entry.grid(row=0, column=1, padx=10, pady=10)
        
        self.key2_label = ctk.CTkLabel(self.key_frame, text="Key 2:")
        self.key2_label.grid(row=0, column=2, padx=10, pady=10, sticky="w")
        
        self.key2_entry = ctk.CTkEntry(self.key_frame, width=300)
        self.key2_entry.grid(row=0, column=3, padx=10, pady=10)
        
        # Generate keys button
        self.generate_btn = ctk.CTkButton(
            self.key_frame,
            text="Generate Keys",
            command=self.generate_keys
        )
        self.generate_btn.grid(row=1, column=0, columnspan=4, pady=10)
        
        # Plaintext input
        ctk.CTkLabel(self, text="Plaintext:").pack(anchor="w", padx=10)
        self.plaintext_text = ctk.CTkTextbox(self, height=150)
        self.plaintext_text.pack(fill="x", padx=10, pady=5)
        self.plaintext_text.insert("1.0", "Enter your plaintext here...")
        
        # Ciphertext output
        ctk.CTkLabel(self, text="Ciphertext / Hash:").pack(anchor="w", padx=10, pady=(10, 0))
        self.ciphertext_text = ctk.CTkTextbox(self, height=150)
        self.ciphertext_text.pack(fill="x", padx=10, pady=5)
        
        # Buttons
        button_frame = ctk.CTkFrame(self)
        button_frame.pack(fill="x", pady=20)
        
        ctk.CTkButton(
            button_frame,
            text="Encrypt / Hash",
            command=self.encrypt,
            width=150
        ).pack(side="left", padx=10)
        
        ctk.CTkButton(
            button_frame,
            text="Decrypt / Verify",
            command=self.decrypt,
            width=150
        ).pack(side="left", padx=10)
        
        ctk.CTkButton(
            button_frame,
            text="Copy Result",
            command=self.copy_result,
            width=120
        ).pack(side="left", padx=10)
        
        ctk.CTkButton(
            button_frame,
            text="Clear",
            command=self.clear_fields,
            fg_color="darkred",
            hover_color="red",
            width=100
        ).pack(side="left", padx=10)
        
        # Initialize key fields
        self.update_key_fields("Caesar Cipher")
    
    def update_key_fields(self, algorithm):
        """Update key field labels based on selected algorithm"""
        algorithm = self.algorithm_var.get()
        
        if algorithm == "Caesar Cipher":
            self.key1_label.configure(text="Shift (0-25):")
            self.key2_label.configure(text="")
            self.key2_entry.grid_remove()
        elif algorithm == "Affine Cipher":
            self.key1_label.configure(text="Key a (1,3,5,7,9,11,15,17,19,21,23,25):")
            self.key2_label.configure(text="Key b (0-25):")
            self.key2_entry.grid(row=0, column=3, padx=10, pady=10)
        elif algorithm in ["Vigenère Cipher", "Playfair Cipher", "Columnar Transposition"]:
            self.key1_label.configure(text="Keyword:")
            self.key2_label.configure(text="")
            self.key2_entry.grid_remove()
        elif algorithm == "Rail Fence Cipher":
            self.key1_label.configure(text="Rails (>=2):")
            self.key2_label.configure(text="")
            self.key2_entry.grid_remove()
        elif algorithm == "AES-256":
            self.key1_label.configure(text="AES Key (auto-generated or paste):")
            self.key2_label.configure(text="")
            self.key2_entry.grid_remove()
        elif algorithm == "RSA-2048":
            self.key1_label.configure(text="Public Key:")
            self.key2_label.configure(text="Private Key:")
            self.key2_entry.grid(row=0, column=3, padx=10, pady=10)
        elif algorithm == "SHA-256":
            self.key1_label.configure(text="(No key needed)")
            self.key2_label.configure(text="")
            self.key1_entry.grid_remove()
            self.key2_entry.grid_remove()
        else:
            self.key1_entry.grid(row=0, column=1, padx=10, pady=10)
    
    def generate_keys(self):
        """Generate keys for modern algorithms"""
        algorithm = self.algorithm_var.get()
        
        try:
            if algorithm == "AES-256":
                from algorithms.modern.aes import generate_key
                self.app.aes_key = generate_key()
                self.key1_entry.delete(0, "end")
                self.key1_entry.insert(0, self.app.aes_key)
            elif algorithm == "RSA-2048":
                from algorithms.modern.rsa import generate_key_pair
                self.app.rsa_public_key, self.app.rsa_private_key = generate_key_pair()
                self.key1_entry.delete(0, "end")
                self.key1_entry.insert(0, self.app.rsa_public_key)
                self.key2_entry.delete(0, "end")
                self.key2_entry.insert(0, self.app.rsa_private_key)
            else:
                messagebox.showinfo("Info", "Key generation is only available for AES and RSA algorithms.")
        except Exception as e:
            messagebox.showerror("Error", f"Error generating keys: {str(e)}")
    
    def encrypt(self):
        """Perform encryption/hashing"""
        algorithm = self.algorithm_var.get()
        plaintext = self.plaintext_text.get("1.0", "end-1c")
        
        try:
            result = ""
            
            if algorithm == "Caesar Cipher":
                from algorithms.classical.caesar import encrypt
                shift = int(self.key1_entry.get())
                result = encrypt(plaintext, shift)
            elif algorithm == "Affine Cipher":
                from algorithms.classical.affine import encrypt
                a = int(self.key1_entry.get())
                b = int(self.key2_entry.get())
                result = encrypt(plaintext, a, b)
            elif algorithm == "Vigenère Cipher":
                from algorithms.classical.vigenere import encrypt
                keyword = self.key1_entry.get()
                result = encrypt(plaintext, keyword)
            elif algorithm == "Playfair Cipher":
                from algorithms.classical.playfair import encrypt
                keyword = self.key1_entry.get()
                result = encrypt(plaintext, keyword)
            elif algorithm == "Rail Fence Cipher":
                from algorithms.classical.rail_fence import encrypt
                rails = int(self.key1_entry.get())
                result = encrypt(plaintext, rails)
            elif algorithm == "Columnar Transposition":
                from algorithms.classical.columnar import encrypt
                keyword = self.key1_entry.get()
                result = encrypt(plaintext, keyword)
            elif algorithm == "AES-256":
                from algorithms.modern.aes import encrypt
                key = self.key1_entry.get()
                result = encrypt(plaintext, key)
            elif algorithm == "RSA-2048":
                from algorithms.modern.rsa import encrypt
                key = self.key1_entry.get()
                result = encrypt(plaintext, key)
            elif algorithm == "SHA-256":
                from algorithms.modern.sha256 import hash_hex
                result = hash_hex(plaintext)
            
            self.ciphertext_text.delete("1.0", "end")
            self.ciphertext_text.insert("1.0", result)
        except Exception as e:
            messagebox.showerror("Error", f"Encryption error: {str(e)}")
    
    def decrypt(self):
        """Perform decryption/verification"""
        algorithm = self.algorithm_var.get()
        ciphertext = self.ciphertext_text.get("1.0", "end-1c")
        
        try:
            result = ""
            
            if algorithm == "Caesar Cipher":
                from algorithms.classical.caesar import decrypt
                shift = int(self.key1_entry.get())
                result = decrypt(ciphertext, shift)
            elif algorithm == "Affine Cipher":
                from algorithms.classical.affine import decrypt
                a = int(self.key1_entry.get())
                b = int(self.key2_entry.get())
                result = decrypt(ciphertext, a, b)
            elif algorithm == "Vigenère Cipher":
                from algorithms.classical.vigenere import decrypt
                keyword = self.key1_entry.get()
                result = decrypt(ciphertext, keyword)
            elif algorithm == "Playfair Cipher":
                from algorithms.classical.playfair import decrypt
                keyword = self.key1_entry.get()
                result = decrypt(ciphertext, keyword)
            elif algorithm == "Rail Fence Cipher":
                from algorithms.classical.rail_fence import decrypt
                rails = int(self.key1_entry.get())
                result = decrypt(ciphertext, rails)
            elif algorithm == "Columnar Transposition":
                from algorithms.classical.columnar import decrypt
                keyword = self.key1_entry.get()
                result = decrypt(ciphertext, keyword)
            elif algorithm == "AES-256":
                from algorithms.modern.aes import decrypt
                key = self.key1_entry.get()
                result = decrypt(ciphertext, key)
            elif algorithm == "RSA-2048":
                from algorithms.modern.rsa import decrypt
                key = self.key2_entry.get()
                result = decrypt(ciphertext, key)
            elif algorithm == "SHA-256":
                from algorithms.modern.sha256 import verify_hex
                plaintext = self.plaintext_text.get("1.0", "end-1c")
                if verify_hex(plaintext, ciphertext):
                    result = "Hash verification: SUCCESS - The hash matches the input."
                else:
                    result = "Hash verification: FAILED - The hash does not match the input."
                messagebox.showinfo("Verification Result", result)
                return
            
            if algorithm != "SHA-256":
                self.plaintext_text.delete("1.0", "end")
                self.plaintext_text.insert("1.0", result)
        except Exception as e:
            messagebox.showerror("Error", f"Decryption error: {str(e)}")
    
    def copy_result(self):
        """Copy result to clipboard"""
        result = self.ciphertext_text.get("1.0", "end-1c")
        if result:
            pyperclip.copy(result)
            messagebox.showinfo("Success", "Result copied to clipboard!")
    
    def clear_fields(self):
        """Clear all fields"""
        self.plaintext_text.delete("1.0", "end")
        self.ciphertext_text.delete("1.0", "end")
        self.key1_entry.delete(0, "end")
        self.key2_entry.delete(0, "end")
