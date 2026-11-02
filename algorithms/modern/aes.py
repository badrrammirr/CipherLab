"""
AES (Advanced Encryption Standard) implementation using PyCryptodome
Supports AES-256 in GCM mode for authenticated encryption
"""

import base64
from Crypto.Cipher import AES
from Crypto.Random import get_random_bytes


KEY_SIZE = 32  # 256 bits
GCM_TAG_LENGTH = 16  # 128 bits
GCM_IV_LENGTH = 12  # 96 bits


def generate_key() -> str:
    """
    Generates a new AES-256 key
    
    Returns:
        Base64-encoded key
    """
    key = get_random_bytes(KEY_SIZE)
    return base64.b64encode(key).decode('utf-8')


def encrypt(plaintext: str, key: str) -> str:
    """
    Encrypts plaintext using AES-256-GCM
    
    Args:
        plaintext: The text to encrypt
        key: Base64-encoded AES key
    
    Returns:
        Base64-encoded ciphertext (IV + ciphertext + tag)
    
    Raises:
        ValueError: if key is invalid
    """
    if not plaintext:
        return plaintext
    
    try:
        key_bytes = base64.b64decode(key)
        
        # Generate random IV
        iv = get_random_bytes(GCM_IV_LENGTH)
        
        cipher = AES.new(key_bytes, AES.MODE_GCM, nonce=iv)
        ciphertext, tag = cipher.encrypt_and_digest(plaintext.encode('utf-8'))
        
        # Combine IV, ciphertext, and tag
        combined = iv + ciphertext + tag
        
        return base64.b64encode(combined).decode('utf-8')
    except Exception as e:
        raise ValueError(f"AES encryption failed: {str(e)}")


def decrypt(ciphertext: str, key: str) -> str:
    """
    Decrypts ciphertext using AES-256-GCM
    
    Args:
        ciphertext: Base64-encoded ciphertext (IV + ciphertext + tag)
        key: Base64-encoded AES key
    
    Returns:
        Decrypted plaintext
    
    Raises:
        ValueError: if key or ciphertext is invalid
    """
    if not ciphertext:
        return ciphertext
    
    try:
        key_bytes = base64.b64decode(key)
        combined = base64.b64decode(ciphertext)
        
        # Extract IV, ciphertext, and tag
        iv = combined[:GCM_IV_LENGTH]
        tag = combined[-GCM_TAG_LENGTH:]
        encrypted_bytes = combined[GCM_IV_LENGTH:-GCM_TAG_LENGTH]
        
        cipher = AES.new(key_bytes, AES.MODE_GCM, nonce=iv)
        decrypted_bytes = cipher.decrypt_and_verify(encrypted_bytes, tag)
        
        return decrypted_bytes.decode('utf-8')
    except Exception as e:
        raise ValueError(f"AES decryption failed: {str(e)}")


def get_key_size() -> int:
    """Gets the key size in bits"""
    return KEY_SIZE * 8
