"""
Rail Fence Cipher implementation
A transposition cipher that writes the message in a zigzag pattern
"""


def encrypt(plaintext: str, rails: int) -> str:
    """
    Encrypts plaintext using Rail Fence cipher
    
    Args:
        plaintext: The text to encrypt
        rails: Number of rails (must be >= 2)
    
    Returns:
        Encrypted ciphertext
    
    Raises:
        ValueError: if rails < 2
    """
    if not plaintext:
        return plaintext
    
    if rails < 2:
        raise ValueError("Number of rails must be at least 2")
    
    if rails >= len(plaintext):
        return plaintext
    
    fence = [[] for _ in range(rails)]
    rail = 0
    direction = 1
    
    for char in plaintext:
        fence[rail].append(char)
        
        if rail == 0:
            direction = 1
        elif rail == rails - 1:
            direction = -1
        
        rail += direction
    
    # Build read order: outside-in (top, bottom, second, second-bottom, ...)
    order = []
    left, right = 0, rails - 1
    while left <= right:
        if left == right:
            order.append(left)
        else:
            order.append(left)
            order.append(right)
        left += 1
        right -= 1

    result = []
    for r in order:
        result.extend(fence[r])

    return ''.join(result)


def decrypt(ciphertext: str, rails: int) -> str:
    """
    Decrypts ciphertext using Rail Fence cipher
    
    Args:
        ciphertext: The text to decrypt
        rails: Number of rails (must be >= 2)
    
    Returns:
        Decrypted plaintext
    
    Raises:
        ValueError: if rails < 2
    """
    if not ciphertext:
        return ciphertext
    
    if rails < 2:
        raise ValueError("Number of rails must be at least 2")
    
    if rails >= len(ciphertext):
        return ciphertext
    
    n = len(ciphertext)
    rail_lengths = [0] * rails
    
    rail = 0
    direction = 1
    
    for i in range(n):
        rail_lengths[rail] += 1
        
        if rail == 0:
            direction = 1
        elif rail == rails - 1:
            direction = -1
        
        rail += direction
    
    order = []
    left, right = 0, rails - 1
    while left <= right:
        if left == right:
            order.append(left)
        else:
            order.append(left)
            order.append(right)
        left += 1
        right -= 1

    fence = [None] * rails
    index = 0
    for r in order:
        fence[r] = list(ciphertext[index:index + rail_lengths[r]])
        index += rail_lengths[r]
    
    result = []
    rail = 0
    direction = 1
    positions = [0] * rails
    
    for i in range(n):
        result.append(fence[rail][positions[rail]])
        positions[rail] += 1
        
        if rail == 0:
            direction = 1
        elif rail == rails - 1:
            direction = -1
        
        rail += direction
    
    return ''.join(result)
