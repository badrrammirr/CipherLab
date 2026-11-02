"""
Algorithm comparison module
"""


def get_algorithm_comparison_data() -> list:
    """
    Returns comprehensive comparison data for all algorithms
    
    Returns:
        List of dictionaries with algorithm information
    """
    return [
        {
            "algorithm": "Caesar Cipher",
            "category": "Classical - Substitution",
            "key_space": "26 (2^4.7)",
            "speed": "Very Fast",
            "security": "Very Low",
            "vulnerability": "Brute-force (26 keys)",
            "applications": "Educational, simple puzzles"
        },
        {
            "algorithm": "Affine Cipher",
            "category": "Classical - Substitution",
            "key_space": "312 (2^8.3)",
            "speed": "Very Fast",
            "security": "Low",
            "vulnerability": "Brute-force (312 keys)",
            "applications": "Educational"
        },
        {
            "algorithm": "Vigenère Cipher",
            "category": "Classical - Polyalphabetic",
            "key_space": "26^k (k=key length)",
            "speed": "Fast",
            "security": "Low-Medium",
            "vulnerability": "Kasiski, Friedman",
            "applications": "Historical, educational"
        },
        {
            "algorithm": "Playfair Cipher",
            "category": "Classical - Digraph",
            "key_space": "25! (~2^84)",
            "speed": "Fast",
            "security": "Medium",
            "vulnerability": "Digraph analysis",
            "applications": "Historical military"
        },
        {
            "algorithm": "Rail Fence",
            "category": "Classical - Transposition",
            "key_space": "n (message length)",
            "speed": "Very Fast",
            "security": "Very Low",
            "vulnerability": "Anagram analysis",
            "applications": "Educational"
        },
        {
            "algorithm": "Columnar Transposition",
            "category": "Classical - Transposition",
            "key_space": "k! (k=key length)",
            "speed": "Fast",
            "security": "Low",
            "vulnerability": "Anagram analysis",
            "applications": "Historical"
        },
        {
            "algorithm": "AES-256",
            "category": "Modern - Symmetric",
            "key_space": "2^256",
            "speed": "Fast",
            "security": "Very High",
            "vulnerability": "None known (quantum: 2^128)",
            "applications": "Government, data encryption"
        },
        {
            "algorithm": "RSA-2048",
            "category": "Modern - Asymmetric",
            "key_space": "2^2048",
            "speed": "Slow",
            "security": "High",
            "vulnerability": "Quantum (Shor's algorithm)",
            "applications": "Digital signatures, key exchange"
        },
        {
            "algorithm": "SHA-256",
            "category": "Modern - Hash",
            "key_space": "N/A (one-way)",
            "speed": "Very Fast",
            "security": "High (collision resistant)",
            "vulnerability": "Collision attacks (theoretical)",
            "applications": "Integrity verification, blockchain"
        }
    ]


def get_key_space_comparison() -> dict:
    """
    Returns key space comparison data for charts
    
    Returns:
        Dictionary with algorithm names and log2 key space values
    """
    return {
        "Caesar": 4.7,
        "Affine": 8.3,
        "Vigenère (k=5)": 26.0,
        "Playfair": 84.0,
        "AES-256": 256.0,
        "RSA-2048": 2048.0
    }


def get_performance_comparison() -> dict:
    """
    Returns relative performance comparison data
    
    Returns:
        Dictionary with algorithm names and relative speed scores
    """
    return {
        "Caesar": 100,
        "Affine": 95,
        "Vigenère": 90,
        "Playfair": 85,
        "Rail Fence": 100,
        "Columnar": 85,
        "AES-256": 70,
        "RSA-2048": 20,
        "SHA-256": 95
    }


def get_security_notes() -> str:
    """
    Returns comprehensive security analysis notes
    
    Returns:
        Formatted string with security information
    """
    notes = """CLASSICAL CIPHERS:
-----------------
• All classical ciphers are vulnerable to modern cryptanalysis
• Caesar and Affine: Extremely small key spaces, easily brute-forced
• Vigenère: Vulnerable to Kasiski examination and frequency analysis
• Playfair: Better than simple substitution but still breakable
• Transposition ciphers: Preserve letter frequencies, vulnerable to anagrams

MODERN CIPHERS:
----------------
• AES-256: Considered secure against all known practical attacks
  - Key space: 2^256 (impossible to brute-force)
  - Used by governments, military, and industry worldwide

• RSA-2048: Secure against classical computers
  - Security based on integer factorization problem
  - Vulnerable to quantum computers (Shor's algorithm)
  - Used for digital signatures and key exchange

• SHA-256: Cryptographic hash function
  - One-way function, preimage resistant
  - Collision resistance: 2^128 operations (theoretical)
  - Used for integrity verification and Bitcoin mining

QUANTUM THREATS:
-----------------
• Grover's algorithm: Reduces security by square root (AES: 2^256 → 2^128)
• Shor's algorithm: Breaks RSA completely
• Post-quantum cryptography is being developed to address these threats"""
    
    return notes
