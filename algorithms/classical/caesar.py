"""
Caesar Cipher implementation
A substitution cipher where each letter is shifted by a fixed number of positions
"""


def encrypt(plaintext: str, shift: int) -> str:
    """
    Encrypts plaintext using Caesar cipher
    
    Args:
        plaintext: The text to encrypt
        shift: The shift value (0-25)
    
    Returns:
        Encrypted ciphertext
    """
    if not plaintext:
        return plaintext
    
    shift = normalize_shift(shift)
    result = []
    
    for char in plaintext:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            shifted = chr((ord(char) - base + shift) % 26 + base)
            result.append(shifted)
        else:
            result.append(char)
    
    return ''.join(result)


def decrypt(ciphertext: str, shift: int) -> str:
    """
    Decrypts ciphertext using Caesar cipher
    
    Args:
        ciphertext: The text to decrypt
        shift: The shift value (0-25)
    
    Returns:
        Decrypted plaintext
    """
    if not ciphertext:
        return ciphertext
    
    shift = normalize_shift(shift)
    return encrypt(ciphertext, 26 - shift)


def normalize_shift(shift: int) -> int:
    """Normalizes shift to be within 0-25 range"""
    shift = shift % 26
    if shift < 0:
        shift += 26
    return shift


def brute_force(ciphertext: str) -> list:
    """
    Performs brute-force attack on Caesar cipher
    
    Args:
        ciphertext: The ciphertext to attack
    
    Returns:
        List of all possible plaintexts (26 possibilities)
    """
    results = []
    for i in range(26):
        results.append(decrypt(ciphertext, i))
    return results
