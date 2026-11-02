"""
RSA (Rivest-Shamir-Adleman) implementation using PyCryptodome
Asymmetric encryption with 2048-bit keys using OAEP padding
"""

import base64
from Crypto.PublicKey import RSA
from Crypto.Cipher import PKCS1_OAEP
from Crypto.Hash import SHA256


KEY_SIZE = 2048


def generate_key_pair() -> tuple:
    """
    Generates a new RSA key pair
    
    Returns:
        Tuple of (public_key, private_key) as Base64-encoded strings
    """
    key = RSA.generate(KEY_SIZE)
    
    public_key = base64.b64encode(key.publickey().export_key()).decode('utf-8')
    private_key = base64.b64encode(key.export_key()).decode('utf-8')
    
    return public_key, private_key


def encrypt(plaintext: str, public_key: str) -> str:
    """
    Encrypts plaintext using RSA public key with OAEP padding
    
    Args:
        plaintext: The text to encrypt
        public_key: Base64-encoded public key
    
    Returns:
        Base64-encoded ciphertext
    
    Raises:
        ValueError: if key is invalid or plaintext too long
    """
    if not plaintext:
        return plaintext
    
    try:
        key_bytes = base64.b64decode(public_key)
        rsa_key = RSA.import_key(key_bytes)
        
        cipher = PKCS1_OAEP.new(rsa_key, hashAlgo=SHA256)
        encrypted_bytes = cipher.encrypt(plaintext.encode('utf-8'))
        
        return base64.b64encode(encrypted_bytes).decode('utf-8')
    except Exception as e:
        raise ValueError(f"RSA encryption failed: {str(e)}")


def decrypt(ciphertext: str, private_key: str) -> str:
    """
    Decrypts ciphertext using RSA private key with OAEP padding
    
    Args:
        ciphertext: Base64-encoded ciphertext
        private_key: Base64-encoded private key
    
    Returns:
        Decrypted plaintext
    
    Raises:
        ValueError: if key or ciphertext is invalid
    """
    if not ciphertext:
        return ciphertext
    
    try:
        key_bytes = base64.b64decode(private_key)
        rsa_key = RSA.import_key(key_bytes)
        
        cipher = PKCS1_OAEP.new(rsa_key, hashAlgo=SHA256)
        decrypted_bytes = cipher.decrypt(base64.b64decode(ciphertext))
        
        return decrypted_bytes.decode('utf-8')
    except Exception as e:
        raise ValueError(f"RSA decryption failed: {str(e)}")


def get_key_size() -> int:
    """Gets the key size in bits"""
    return KEY_SIZE
