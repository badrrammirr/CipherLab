"""
Performance benchmarking module for cryptographic algorithms
"""

import time
from algorithms.classical.caesar import encrypt as caesar_encrypt, decrypt as caesar_decrypt
from algorithms.classical.affine import encrypt as affine_encrypt, decrypt as affine_decrypt
from algorithms.classical.vigenere import encrypt as vigenere_encrypt, decrypt as vigenere_decrypt
from algorithms.classical.playfair import encrypt as playfair_encrypt, decrypt as playfair_decrypt
from algorithms.classical.rail_fence import encrypt as rail_fence_encrypt, decrypt as rail_fence_decrypt
from algorithms.classical.columnar import encrypt as columnar_encrypt, decrypt as columnar_decrypt
from algorithms.modern.aes import encrypt as aes_encrypt, decrypt as aes_decrypt, generate_key as aes_generate_key
from algorithms.modern.rsa import encrypt as rsa_encrypt, decrypt as rsa_decrypt, generate_key_pair as rsa_generate_key_pair
from algorithms.modern.sha256 import hash as sha256_hash


class BenchmarkResult:
    """Class to store benchmark results"""
    def __init__(self, algorithm: str, encrypt_time: float, decrypt_time: float):
        self.algorithm = algorithm
        self.encrypt_time = encrypt_time
        self.decrypt_time = decrypt_time


def benchmark_algorithm(algorithm: str, input_text: str, iterations: int = 10, 
                        aes_key: str = None, rsa_public_key: str = None, 
                        rsa_private_key: str = None) -> BenchmarkResult:
    """
    Benchmarks a single cryptographic algorithm
    
    Args:
        algorithm: Name of the algorithm to benchmark
        input_text: Text to use for benchmarking
        iterations: Number of iterations to run
        aes_key: AES key (if benchmarking AES)
        rsa_public_key: RSA public key (if benchmarking RSA)
        rsa_private_key: RSA private key (if benchmarking RSA)
    
    Returns:
        BenchmarkResult object with timing data
    """
    encrypt_time = 0.0
    decrypt_time = 0.0
    
    for _ in range(iterations):
        try:
            # Encryption
            start = time.perf_counter()
            encrypted = _encrypt(algorithm, input_text, aes_key, rsa_public_key)
            end = time.perf_counter()
            encrypt_time += (end - start)
            
            # Decryption (if applicable)
            if algorithm != "SHA-256":
                start = time.perf_counter()
                _decrypt(algorithm, encrypted, aes_key, rsa_private_key)
                end = time.perf_counter()
                decrypt_time += (end - start)
        except Exception:
            pass
    
    avg_encrypt_ms = (encrypt_time / iterations) * 1000
    avg_decrypt_ms = (decrypt_time / iterations) * 1000 if algorithm != "SHA-256" else -1
    
    return BenchmarkResult(algorithm, avg_encrypt_ms, avg_decrypt_ms)


def _encrypt(algorithm: str, input_text: str, aes_key: str = None, rsa_public_key: str = None) -> str:
    """Helper function to encrypt with the specified algorithm"""
    if algorithm == "Caesar Cipher":
        return caesar_encrypt(input_text, 3)
    elif algorithm == "Affine Cipher":
        return affine_encrypt(input_text, 5, 7)
    elif algorithm == "Vigenère Cipher":
        return vigenere_encrypt(input_text, "KEY")
    elif algorithm == "Playfair Cipher":
        return playfair_encrypt(input_text, "KEYWORD")
    elif algorithm == "Rail Fence Cipher":
        return rail_fence_encrypt(input_text, 3)
    elif algorithm == "Columnar Transposition":
        return columnar_encrypt(input_text, "KEY")
    elif algorithm == "AES-256":
        return aes_encrypt(input_text, aes_key)
    elif algorithm == "RSA-2048":
        # RSA can only encrypt short messages
        short_input = input_text[:100] if len(input_text) > 100 else input_text
        return rsa_encrypt(short_input, rsa_public_key)
    elif algorithm == "SHA-256":
        return sha256_hash(input_text)
    else:
        raise ValueError(f"Unknown algorithm: {algorithm}")


def _decrypt(algorithm: str, ciphertext: str, aes_key: str = None, rsa_private_key: str = None) -> str:
    """Helper function to decrypt with the specified algorithm"""
    if algorithm == "Caesar Cipher":
        return caesar_decrypt(ciphertext, 3)
    elif algorithm == "Affine Cipher":
        return affine_decrypt(ciphertext, 5, 7)
    elif algorithm == "Vigenère Cipher":
        return vigenere_decrypt(ciphertext, "KEY")
    elif algorithm == "Playfair Cipher":
        return playfair_decrypt(ciphertext, "KEYWORD")
    elif algorithm == "Rail Fence Cipher":
        return rail_fence_decrypt(ciphertext, 3)
    elif algorithm == "Columnar Transposition":
        return columnar_decrypt(ciphertext, "KEY")
    elif algorithm == "AES-256":
        return aes_decrypt(ciphertext, aes_key)
    elif algorithm == "RSA-2048":
        return rsa_decrypt(ciphertext, rsa_private_key)
    else:
        raise ValueError(f"Unknown algorithm: {algorithm}")


def benchmark_all(input_text: str, iterations: int = 10) -> list:
    """
    Benchmarks all algorithms
    
    Args:
        input_text: Text to use for benchmarking
        iterations: Number of iterations to run
    
    Returns:
        List of BenchmarkResult objects
    """
    # Generate keys for modern algorithms
    aes_key = aes_generate_key()
    rsa_public_key, rsa_private_key = rsa_generate_key_pair()
    
    algorithms = [
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
    
    results = []
    for algorithm in algorithms:
        result = benchmark_algorithm(algorithm, input_text, iterations, 
                                     aes_key, rsa_public_key, rsa_private_key)
        results.append(result)
    
    return results
