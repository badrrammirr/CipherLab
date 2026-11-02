"""
Playfair Cipher implementation
A digraph substitution cipher using a 5x5 key matrix
"""

MATRIX = [[None for _ in range(5)] for _ in range(5)]


def encrypt(plaintext: str, keyword: str) -> str:
    """
    Encrypts plaintext using Playfair cipher
    
    Args:
        plaintext: The text to encrypt
        keyword: The keyword for the key matrix
    
    Returns:
        Encrypted ciphertext
    """
    if not plaintext:
        return plaintext
    
    if not keyword:
        raise ValueError("Keyword cannot be empty")
    
    create_matrix(keyword)
    processed = preprocess_text(plaintext)
    result = []
    
    for i in range(0, len(processed), 2):
        a = processed[i]
        b = processed[i + 1]
        
        pos_a = find_position(a)
        pos_b = find_position(b)
        
        if pos_a[0] == pos_b[0]:
            # Same row: shift right
            result.append(MATRIX[pos_a[0]][(pos_a[1] + 1) % 5])
            result.append(MATRIX[pos_b[0]][(pos_b[1] + 1) % 5])
        elif pos_a[1] == pos_b[1]:
            # Same column: shift down
            result.append(MATRIX[(pos_a[0] + 1) % 5][pos_a[1]])
            result.append(MATRIX[(pos_b[0] + 1) % 5][pos_b[1]])
        else:
            # Rectangle: swap columns
            result.append(MATRIX[pos_a[0]][pos_b[1]])
            result.append(MATRIX[pos_b[0]][pos_a[1]])
    
    return ''.join(result)


def decrypt(ciphertext: str, keyword: str) -> str:
    """
    Decrypts ciphertext using Playfair cipher
    
    Args:
        ciphertext: The text to decrypt
        keyword: The keyword for the key matrix
    
    Returns:
        Decrypted plaintext
    """
    if not ciphertext:
        return ciphertext
    
    if not keyword:
        raise ValueError("Keyword cannot be empty")
    
    create_matrix(keyword)
    processed = preprocess_text(ciphertext)
    result = []
    
    for i in range(0, len(processed), 2):
        a = processed[i]
        b = processed[i + 1]
        
        pos_a = find_position(a)
        pos_b = find_position(b)
        
        if pos_a[0] == pos_b[0]:
            # Same row: shift left
            result.append(MATRIX[pos_a[0]][(pos_a[1] + 4) % 5])
            result.append(MATRIX[pos_b[0]][(pos_b[1] + 4) % 5])
        elif pos_a[1] == pos_b[1]:
            # Same column: shift up
            result.append(MATRIX[(pos_a[0] + 4) % 5][pos_a[1]])
            result.append(MATRIX[(pos_b[0] + 4) % 5][pos_b[1]])
        else:
            # Rectangle: swap columns
            result.append(MATRIX[pos_a[0]][pos_b[1]])
            result.append(MATRIX[pos_b[0]][pos_a[1]])
    
    return ''.join(result)


def create_matrix(keyword: str):
    """Creates the 5x5 key matrix from the keyword"""
    global MATRIX
    
    clean_keyword = ''.join(c for c in keyword.upper() if c.isalpha())
    alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ"  # I/J combined
    
    # Remove duplicate letters from keyword
    unique_keyword = []
    seen = set()
    
    for c in clean_keyword:
        if c not in seen:
            seen.add(c)
            unique_keyword.append(c)
            alphabet = alphabet.replace(c, '')
    
    key_string = ''.join(unique_keyword) + alphabet
    
    index = 0
    for i in range(5):
        for j in range(5):
            MATRIX[i][j] = key_string[index]
            index += 1


def find_position(c: str) -> tuple:
    """Finds the position of a character in the matrix"""
    global MATRIX
    
    c = c.upper()
    if c == 'J':
        c = 'I'
    
    for i in range(5):
        for j in range(5):
            if MATRIX[i][j] == c:
                return (i, j)
    
    raise ValueError(f"Character not found in matrix: {c}")


def preprocess_text(text: str) -> str:
    """
    Preprocesses text: uppercase, remove non-letters, insert X for double letters, pad if odd
    """
    text = ''.join(c for c in text.upper() if c.isalpha()).replace('J', 'I')
    
    processed = []
    for i in range(len(text)):
        processed.append(text[i])
        if i + 1 < len(text) and text[i] == text[i + 1]:
            processed.append('X')
    
    if len(processed) % 2 != 0:
        processed.append('X')
    
    return ''.join(processed)
