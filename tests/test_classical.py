"""
Tests for classical cryptography algorithms
"""

import pytest
from algorithms.classical.caesar import encrypt as caesar_encrypt, decrypt as caesar_decrypt, brute_force
from algorithms.classical.affine import encrypt as affine_encrypt, decrypt as affine_decrypt, is_valid_key_a
from algorithms.classical.vigenere import encrypt as vigenere_encrypt, decrypt as vigenere_decrypt
from algorithms.classical.playfair import encrypt as playfair_encrypt, decrypt as playfair_decrypt
from algorithms.classical.rail_fence import encrypt as rail_fence_encrypt, decrypt as rail_fence_decrypt
from algorithms.classical.columnar import encrypt as columnar_encrypt, decrypt as columnar_decrypt


class TestCaesarCipher:
    """Test Caesar cipher"""
    
    def test_encrypt(self):
        assert caesar_encrypt("HELLO", 3) == "KHOOR"
        assert caesar_encrypt("hello", 3) == "khoor"
        assert caesar_encrypt("Hello World!", 3) == "Khoor Zruog!"
    
    def test_decrypt(self):
        assert caesar_decrypt("KHOOR", 3) == "HELLO"
        assert caesar_decrypt("khoor", 3) == "hello"
    
    def test_round_trip(self):
        plaintext = "The quick brown fox jumps over the lazy dog"
        for shift in range(26):
            assert caesar_decrypt(caesar_encrypt(plaintext, shift), shift) == plaintext
    
    def test_brute_force(self):
        ciphertext = caesar_encrypt("SECRET", 5)
        results = brute_force(ciphertext)
        assert len(results) == 26
        assert "SECRET" in results


class TestAffineCipher:
    """Test Affine cipher"""
    
    def test_encrypt(self):
        assert affine_encrypt("HELLO", 5, 7) == "RCLLA"
    
    def test_decrypt(self):
        assert affine_decrypt("RCLLA", 5, 7) == "HELLO"
    
    def test_round_trip(self):
        plaintext = "HELLO WORLD"
        for a in [1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25]:
            for b in range(26):
                assert affine_decrypt(affine_encrypt(plaintext, a, b), a, b) == plaintext
    
    def test_valid_key_a(self):
        assert is_valid_key_a(1) == True
        assert is_valid_key_a(3) == True
        assert is_valid_key_a(5) == True
        assert is_valid_key_a(2) == False
        assert is_valid_key_a(4) == False
    
    def test_invalid_key_a(self):
        with pytest.raises(ValueError):
            affine_encrypt("HELLO", 2, 5)


class TestVigenereCipher:
    """Test Vigenère cipher"""
    
    def test_encrypt(self):
        assert vigenere_encrypt("HELLO", "KEY") == "RIJVS"
    
    def test_decrypt(self):
        assert vigenere_decrypt("RIJVS", "KEY") == "HELLO"
    
    def test_round_trip(self):
        plaintext = "ATTACKATDAWN"
        keyword = "LEMON"
        assert vigenere_decrypt(vigenere_encrypt(plaintext, keyword), keyword) == plaintext
    
    def test_empty_keyword(self):
        with pytest.raises(ValueError):
            vigenere_encrypt("HELLO", "")


class TestPlayfairCipher:
    """Test Playfair cipher"""
    
    def test_encrypt(self):
        ciphertext = playfair_encrypt("HELLO", "KEYWORD")
        assert len(ciphertext) % 2 == 0
    
    def test_decrypt(self):
        plaintext = "HELLO"
        keyword = "KEYWORD"
        ciphertext = playfair_encrypt(plaintext, keyword)
        decrypted = playfair_decrypt(ciphertext, keyword)
        # Playfair may insert X for double letters, so we check if it's close
        assert decrypted.replace("X", "") == plaintext.replace("X", "")
    
    def test_empty_keyword(self):
        with pytest.raises(ValueError):
            playfair_encrypt("HELLO", "")


class TestRailFenceCipher:
    """Test Rail Fence cipher"""
    
    def test_encrypt(self):
        assert rail_fence_encrypt("HELLO", 3) == "HOLEL"
    
    def test_decrypt(self):
        assert rail_fence_decrypt("HOLEL", 3) == "HELLO"
    
    def test_round_trip(self):
        plaintext = "WEAREDISCOVEREDFLEEATONCE"
        for rails in range(2, 6):
            if rails < len(plaintext):
                assert rail_fence_decrypt(rail_fence_encrypt(plaintext, rails), rails) == plaintext
    
    def test_invalid_rails(self):
        with pytest.raises(ValueError):
            rail_fence_encrypt("HELLO", 1)


class TestColumnarTransposition:
    """Test Columnar Transposition cipher"""
    
    def test_encrypt(self):
        ciphertext = columnar_encrypt("HELLO", "KEY")
        assert len(ciphertext) > 0
    
    def test_decrypt(self):
        plaintext = "HELLO"
        keyword = "KEY"
        ciphertext = columnar_encrypt(plaintext, keyword)
        decrypted = columnar_decrypt(ciphertext, keyword)
        # May have padding X
        assert decrypted.replace("X", "") == plaintext.replace("X", "")
    
    def test_empty_keyword(self):
        with pytest.raises(ValueError):
            columnar_encrypt("HELLO", "")
