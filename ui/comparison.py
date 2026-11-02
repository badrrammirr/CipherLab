"""
Comparison Dashboard UI
"""

import customtkinter as ctk
from tkinter import ttk
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from comparison.comparison import (
    get_algorithm_comparison_data,
    get_key_space_comparison,
    get_performance_comparison,
    get_security_notes
)


class ComparisonFrame(ctk.CTkScrollableFrame):
    """Frame for algorithm comparison dashboard"""
    
    def __init__(self, master):
        super().__init__(master)
        
        # Title
        title = ctk.CTkLabel(
            self,
            text="Algorithm Comparison Dashboard",
            font=("Roboto", 24, "bold")
        )
        title.pack(pady=(20, 20))
        
        # Comparison table
        self.create_comparison_table()
        
        # Charts area
        charts_frame = ctk.CTkFrame(self)
        charts_frame.pack(fill="both", expand=True, pady=20)
        
        # Key space chart
        key_space_frame = ctk.CTkFrame(charts_frame)
        key_space_frame.pack(side="left", fill="both", expand=True, padx=10)
        self.create_key_space_chart(key_space_frame)
        
        # Performance chart
        performance_frame = ctk.CTkFrame(charts_frame)
        performance_frame.pack(side="right", fill="both", expand=True, padx=10)
        self.create_performance_chart(performance_frame)
        
        # Security notes
        self.create_security_notes()
    
    def create_comparison_table(self):
        """Create comparison table"""
        table_frame = ctk.CTkFrame(self)
        table_frame.pack(fill="x", pady=10)
        
        columns = ("Algorithm", "Category", "Key Space", "Speed", "Security", "Vulnerability", "Applications")
        self.tree = ttk.Treeview(table_frame, columns=columns, show="headings", height=6)
        
        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=150)
        
        scrollbar = ttk.Scrollbar(table_frame, orient="vertical", command=self.tree.yview)
        self.tree.configure(yscrollcommand=scrollbar.set)
        
        self.tree.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")
        
        # Populate table
        data = get_algorithm_comparison_data()
        for item in data:
            self.tree.insert("", "end", values=(
                item["algorithm"],
                item["category"],
                item["key_space"],
                item["speed"],
                item["security"],
                item["vulnerability"],
                item["applications"]
            ))
    
    def create_key_space_chart(self, parent):
        """Create key space comparison chart"""
        ctk.CTkLabel(parent, text="Key Space Comparison (Log Scale)", font=("Roboto", 12, "bold")).pack(pady=10)
        
        data = get_key_space_comparison()
        
        fig, ax = plt.subplots(figsize=(5, 4))
        ax.bar(data.keys(), data.values(), color='#1f6aa5')
        ax.set_xlabel('Algorithm')
        ax.set_ylabel('Key Space (log2 bits)')
        ax.tick_params(axis='x', rotation=45)
        
        fig.tight_layout()
        
        canvas = FigureCanvasTkAgg(fig, master=parent)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True, padx=5, pady=5)
    
    def create_performance_chart(self, parent):
        """Create performance comparison chart"""
        ctk.CTkLabel(parent, text="Relative Performance", font=("Roboto", 12, "bold")).pack(pady=10)
        
        data = get_performance_comparison()
        
        fig, ax = plt.subplots(figsize=(5, 4))
        ax.bar(data.keys(), data.values(), color='#2d8a4e')
        ax.set_xlabel('Algorithm')
        ax.set_ylabel('Relative Speed')
        ax.tick_params(axis='x', rotation=45)
        
        fig.tight_layout()
        
        canvas = FigureCanvasTkAgg(fig, master=parent)
        canvas.draw()
        canvas.get_tk_widget().pack(fill="both", expand=True, padx=5, pady=5)
    
    def create_security_notes(self):
        """Create security notes section"""
        notes_frame = ctk.CTkFrame(self)
        notes_frame.pack(fill="x", pady=10)
        
        ctk.CTkLabel(notes_frame, text="Security Analysis Notes", font=("Roboto", 14, "bold")).pack(pady=10)
        
        notes_text = ctk.CTkTextbox(notes_frame, height=200)
        notes_text.pack(fill="x", padx=10, pady=10)
        
        notes = get_security_notes()
        notes_text.insert("1.0", notes)
