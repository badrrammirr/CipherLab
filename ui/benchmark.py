"""
Performance Benchmarking UI
"""

import customtkinter as ctk
from tkinter import messagebox, ttk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from benchmark.performance import benchmark_algorithm, benchmark_all


class BenchmarkFrame(ctk.CTkScrollableFrame):
    """Frame for performance benchmarking"""
    
    def __init__(self, master, app):
        super().__init__(master)
        self.app = app
        
        # Title
        title = ctk.CTkLabel(
            self,
            text="Performance Benchmarking",
            font=("Roboto", 24, "bold")
        )
        title.pack(pady=(20, 20))
        
        # Input section
        input_frame = ctk.CTkFrame(self)
        input_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(input_frame, text="Input Text:").pack(anchor="w", padx=10)
        self.input_text = ctk.CTkTextbox(input_frame, height=100)
        self.input_text.pack(fill="x", padx=10, pady=5)
        self.input_text.insert("1.0", "The quick brown fox jumps over the lazy dog. This is a sample text for benchmarking cryptographic algorithms.")
        
        # Controls
        control_frame = ctk.CTkFrame(self)
        control_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(control_frame, text="Algorithm:").pack(side="left", padx=10)
        
        self.algorithm_var = ctk.StringVar(value="All Algorithms")
        self.algorithm_combo = ctk.CTkOptionMenu(
            control_frame,
            variable=self.algorithm_var,
            values=[
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
            ]
        )
        self.algorithm_combo.pack(side="left", padx=10)
        
        ctk.CTkLabel(control_frame, text="Iterations:").pack(side="left", padx=10)
        self.iterations_entry = ctk.CTkEntry(control_frame, width=80)
        self.iterations_entry.insert(0, "10")
        self.iterations_entry.pack(side="left", padx=10)
        
        ctk.CTkButton(
            control_frame,
            text="Run Benchmark",
            command=self.run_benchmark,
            width=150
        ).pack(side="left", padx=10)
        
        ctk.CTkButton(
            control_frame,
            text="Compare All",
            command=self.compare_all,
            fg_color="#2d8a4e",
            hover_color="#3da85e",
            width=150
        ).pack(side="left", padx=10)
        
        # Results table
        self.create_results_table()
        
        # Chart area
        self.chart_frame = ctk.CTkFrame(self)
        self.chart_frame.pack(fill="both", expand=True, pady=20)
    
    def create_results_table(self):
        """Create results table"""
        table_frame = ctk.CTkFrame(self)
        table_frame.pack(fill="x", pady=10)
        
        columns = ("Algorithm", "Encrypt Time (ms)", "Decrypt Time (ms)", "Avg Time (ms)")
        self.tree = ttk.Treeview(table_frame, columns=columns, show="headings", height=8)
        
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=200)
        
        scrollbar = ttk.Scrollbar(table_frame, orient="vertical", command=self.tree.yview)
        self.tree.configure(yscrollcommand=scrollbar.set)
        
        self.tree.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")
    
    def run_benchmark(self):
        """Run benchmark for selected algorithm"""
        algorithm = self.algorithm_var.get()
        input_text = self.input_text.get("1.0", "end-1c")
        
        if not input_text:
            messagebox.showerror("Error", "Please enter input text for benchmarking.")
            return
        
        try:
            iterations = int(self.iterations_entry.get())
            if iterations < 1:
                raise ValueError()
        except ValueError:
            messagebox.showerror("Error", "Please enter a valid number of iterations (>= 1).")
            return
        
        try:
            # Generate keys if needed
            if self.app.aes_key is None:
                from algorithms.modern.aes import generate_key
                self.app.aes_key = generate_key()
            if self.app.rsa_public_key is None or self.app.rsa_private_key is None:
                from algorithms.modern.rsa import generate_key_pair
                self.app.rsa_public_key, self.app.rsa_private_key = generate_key_pair()
            
            if algorithm == "All Algorithms":
                results = benchmark_all(input_text, iterations)
            else:
                result = benchmark_algorithm(
                    algorithm, input_text, iterations,
                    self.app.aes_key, self.app.rsa_public_key, self.app.rsa_private_key
                )
                results = [result]
            
            self.display_results(results)
            self.update_chart(results)
            
        except Exception as e:
            messagebox.showerror("Error", f"Benchmark error: {str(e)}")
    
    def compare_all(self):
        """Compare all algorithms"""
        self.algorithm_var.set("All Algorithms")
        self.run_benchmark()
    
    def display_results(self, results):
        """Display results in table"""
        # Clear existing items
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        # Add new items
        for result in results:
            avg_time = (result.encrypt_time + max(0, result.decrypt_time)) / 2
            decrypt_str = f"{result.decrypt_time:.4f}" if result.decrypt_time >= 0 else "N/A"
            self.tree.insert("", "end", values=(
                result.algorithm,
                f"{result.encrypt_time:.4f}",
                decrypt_str,
                f"{avg_time:.4f}"
            ))
    
    def update_chart(self, results):
        """Update performance chart"""
        # Clear previous chart
        for widget in self.chart_frame.winfo_children():
            widget.destroy()
        
        # Create matplotlib figure
        fig, ax = plt.subplots(figsize=(10, 4))
        
        algorithms = [r.algorithm for r in results]
        encrypt_times = [r.encrypt_time for r in results]
        decrypt_times = [r.decrypt_time if r.decrypt_time >= 0 else 0 for r in results]
        
        x = range(len(algorithms))
        width = 0.35
        
        ax.bar([i - width/2 for i in x], encrypt_times, width, label='Encryption', color='#1f6aa5')
        ax.bar([i + width/2 for i in x], decrypt_times, width, label='Decryption', color='#2d8a4e')
        
        ax.set_xlabel('Algorithm')
        ax.set_ylabel('Time (ms)')
        ax.set_title('Algorithm Performance Comparison')
        ax.set_xticks(x)
        ax.set_xticklabels(algorithms, rotation=45, ha='right')
        ax.legend()
        
        fig.tight_layout()
        
        # Embed in tkinter
        canvas = FigureCanvasTkAgg(fig, master=self.chart_frame)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True)
