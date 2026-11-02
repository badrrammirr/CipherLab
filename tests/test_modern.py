"""
Tests for modern cryptography algorithms
"""

import pytest
from algorithms.modern.aes import generate_key, encrypt as aes_encrypt, decrypt as aes_decrypt
from algorithms.modern.rsa import generate_key_pair, encrypt as rsa_encrypt, decrypt as rsa_decrypt
from algorithms.modern.sha256 import hash, hash_hex, verify, verify_hex


class TestAES:
    """Test AES encryption"""
    
    def test_generate_key(self):
        key = generate_key()
        assert key is not None
        assert len(key) > 0
    
    def test_encrypt_decrypt(self):
        plaintext = "Hello, World!"
        key = generate_key()
        ciphertext = aes_encrypt(plaintext, key)
        decrypted = aes_decrypt(ciphertext, key)
        assert decrypted == plaintext
    
    def test_round_trip(self):
        plaintext = "The quick brown fox jumps over the lazy dog"
        key = generate_key()
        assert aes_decrypt(aes_encrypt(plaintext, key), key) == plaintext
    
    def test_empty_plaintext(self):
        key = generate_key()
        assert aes_encrypt("", key) == ""
        assert aes_decrypt("", key) == ""


class TestRSA:
    """Test RSA encryption"""
    
    def test_generate_key_pair(self):
        public_key, private_key = generate_key_pair()
        assert public_key is not None
        assert private_key is not None
        assert public_key != private_key
    
    def test_encrypt_decrypt(self):
        plaintext = "Hello"
        public_key, private_key = generate_key_pair()
        ciphertext = rsa_encrypt(plaintext, public_key)
        decrypted = rsa_decrypt(ciphertext, private_key)
        assert decrypted == plaintext
    
    def test_round_trip(self):
        plaintext = "Test message"
        public_key, private_key = generate_key_pair()
        assert rsa_decrypt(rsa_encrypt(plaintext, public_key), private_key) == plaintext
    
    def test_empty_plaintext(self):
        public_key, private_key = generate_key_pair()
        assert rsa_encrypt("", public_key) == ""
        assert rsa_decrypt("", private_key) == ""


class TestSHA256:
    """Test SHA-256 hashing"""
    
    def test_hash(self):
        plaintext = "Hello, World!"
        hash_value = hash(plaintext)
        assert hash_value is not None
        assert len(hash_value) > 0
    
    def test_hash_hex(self):
        plaintext = "Hello, World!"
        hash_value = hash_hex(plaintext)
        assert hash_value is not None
        assert len(hash_value) == 64  # SHA-256 produces 64 hex characters
    
    def test_verify(self):
        plaintext = "Hello, World!"
        hash_value = hash(plaintext)
        assert verify(plaintext, hash_value) == True
        assert verify("Different text", hash_value) == False
    
    def test_verify_hex(self):
        plaintext = "Hello, World!"
        hash_value = hash_hex(plaintext)
        assert verify_hex(plaintext, hash_value) == True
        assert verify_hex("Different text", hash_value) == False
    
    def test_consistency(self):
        plaintext = "Test"
        hash1 = hash_hex(plaintext)
        hash2 = hash_hex(plaintext)
        assert hash1 == hash2
    
    def test_different_inputs(self):
        hash1 = hash_hex("Hello")
        hash2 = hash_hex("World")
        assert hash1 != hash2
