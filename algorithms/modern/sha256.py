"""
SHA-256 cryptographic hash implementation using hashlib
"""

import hashlib
import base64


def hash(input_text: str) -> str:
    """
    Computes SHA-256 hash of the input
    
    Args:
        input_text: The text to hash
    
    Returns:
        Base64-encoded hash
    """
    if input_text is None:
        input_text = ""
    
    hash_bytes = hashlib.sha256(input_text.encode('utf-8')).digest()
    return base64.b64encode(hash_bytes).decode('utf-8')


def hash_hex(input_text: str) -> str:
    """
    Computes SHA-256 hash and returns as hexadecimal string
    
    Args:
        input_text: The text to hash
    
    Returns:
        Hexadecimal hash string
    """
    if input_text is None:
        input_text = ""
    
    return hashlib.sha256(input_text.encode('utf-8')).hexdigest()


def verify(input_text: str, hash_value: str) -> bool:
    """
    Verifies if the input matches the given hash
    
    Args:
        input_text: The text to verify
        hash_value: Base64-encoded hash to compare against
    
    Returns:
        True if the hash matches, False otherwise
    """
    computed_hash = hash(input_text)
    return computed_hash == hash_value


def verify_hex(input_text: str, hash_hex_value: str) -> bool:
    """
    Verifies if the input matches the given hexadecimal hash

    Args:
        input_text: The text to verify
        hash_hex_value: Hexadecimal hash to compare against

    Returns:
        True if the hash matches, False otherwise
    """
    computed_hash = hash_hex(input_text)
    return computed_hash.lower() == hash_hex_value.lower()


def get_hash_length() -> int:
    """Gets the hash length in bytes"""
    return 32  # SHA-256 produces 256-bit = 32-byte hash
