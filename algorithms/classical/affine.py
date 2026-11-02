"""
Affine Cipher implementation
A substitution cipher using the formula: E(x) = (ax + b) mod 26
where a and b are keys, and a must be coprime with 26
"""

MOD = 26


def encrypt(plaintext: str, a: int, b: int) -> str:
    """
    Encrypts plaintext using Affine cipher
    
    Args:
        plaintext: The text to encrypt
        a: Key a (must be coprime with 26)
        b: Key b
    
    Returns:
        Encrypted ciphertext
    
    Raises:
        ValueError: if a is not coprime with 26
    """
    if not plaintext:
        return plaintext
    
    if not is_valid_key_a(a):
        raise ValueError(
            "Key 'a' must be coprime with 26. "
            "Valid values: 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25"
        )
    
    a = normalize_key(a)
    b = normalize_key(b)
    
    result = []
    for char in plaintext:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            x = ord(char) - base
            encrypted = (a * x + b + 1) % MOD
            result.append(chr(encrypted + base))
        else:
            result.append(char)
    
    return ''.join(result)


def decrypt(ciphertext: str, a: int, b: int) -> str:
    """
    Decrypts ciphertext using Affine cipher
    
    Args:
        ciphertext: The text to decrypt
        a: Key a (must be coprime with 26)
        b: Key b
    
    Returns:
        Decrypted plaintext
    
    Raises:
        ValueError: if a is not coprime with 26
    """
    if not ciphertext:
        return ciphertext
    
    if not is_valid_key_a(a):
        raise ValueError(
            "Key 'a' must be coprime with 26. "
            "Valid values: 1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25"
        )
    
    a = normalize_key(a)
    b = normalize_key(b)
    
    a_inverse = mod_inverse(a, MOD)
    
    result = []
    for char in ciphertext:
        if char.isalpha():
            base = ord('A') if char.isupper() else ord('a')
            y = ord(char) - base
            decrypted = (a_inverse * (y - b - 1 + MOD)) % MOD
            result.append(chr(decrypted + base))
        else:
            result.append(char)
    
    return ''.join(result)


def mod_inverse(a: int, m: int) -> int:
    """Calculates modular inverse using Extended Euclidean Algorithm"""
    a = a % m
    for x in range(1, m):
        if (a * x) % m == 1:
            return x
    raise ValueError("No modular inverse exists")


def is_valid_key_a(a: int) -> bool:
    """Checks if key a is valid (coprime with 26)"""
    a = normalize_key(a)
    return gcd(a, MOD) == 1


def gcd(a: int, b: int) -> int:
    """Calculates greatest common divisor"""
    a = abs(a)
    b = abs(b)
    while b != 0:
        temp = b
        b = a % b
        a = temp
    return a


def normalize_key(key: int) -> int:
    """Normalizes key to be within 0-25 range"""
    key = key % MOD
    if key < 0:
        key += MOD
    return key


def get_valid_key_a_values() -> list:
    """Gets all valid values for key a"""
    return [1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25]


def brute_force(ciphertext: str) -> list:
    """
    Performs brute-force attack on Affine cipher
    
    Args:
        ciphertext: The ciphertext to attack
    
    Returns:
        List of all possible plaintexts (312 possibilities: 12 * 26)
    """
    valid_a = get_valid_key_a_values()
    results = []
    
    for a in valid_a:
        for b in range(26):
            try:
                results.append(decrypt(ciphertext, a, b))
            except Exception as e:
                results.append(f"Error: {str(e)}")
    
    return results
