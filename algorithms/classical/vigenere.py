"""
Vigenère Cipher implementation
A polyalphabetic substitution cipher using a keyword
"""


def encrypt(plaintext: str, keyword: str) -> str:
    """
    Encrypts plaintext using Vigenère cipher
    
    Args:
        plaintext: The text to encrypt
        keyword: The keyword for encryption
    
    Returns:
        Encrypted ciphertext
    
    Raises:
        ValueError: if keyword is empty or contains no letters
    """
    if not plaintext:
        return plaintext
    
    if not keyword:
        raise ValueError("Keyword cannot be empty")
    
    clean_keyword = ''.join(c for c in keyword if c.isalpha())
    if not clean_keyword:
        raise ValueError("Keyword must contain at least one letter")
    
    result = []
    key_index = 0
    
    for char in plaintext:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            plain_shift = ord(char) - base
            key_shift = ord(clean_keyword[key_index % len(clean_keyword)].upper()) - ord('A')
            encrypted = (plain_shift + key_shift) % 26
            result.append(chr(encrypted + base))
            key_index += 1
        else:
            result.append(char)
    
    return ''.join(result)


def decrypt(ciphertext: str, keyword: str) -> str:
    """
    Decrypts ciphertext using Vigenère cipher
    
    Args:
        ciphertext: The text to decrypt
        keyword: The keyword for decryption
    
    Returns:
        Decrypted plaintext
    
    Raises:
        ValueError: if keyword is empty or contains no letters
    """
    if not ciphertext:
        return ciphertext
    
    if not keyword:
        raise ValueError("Keyword cannot be empty")
    
    clean_keyword = ''.join(c for c in keyword if c.isalpha())
    if not clean_keyword:
        raise ValueError("Keyword must contain at least one letter")
    
    result = []
    key_index = 0
    
    for char in ciphertext:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            cipher_shift = ord(char) - base
            key_shift = ord(clean_keyword[key_index % len(clean_keyword)].upper()) - ord('A')
            decrypted = (cipher_shift - key_shift + 26) % 26
            result.append(chr(decrypted + base))
            key_index += 1
        else:
            result.append(char)
    
    return ''.join(result)
