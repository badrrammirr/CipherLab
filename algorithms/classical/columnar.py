"""
Columnar Transposition Cipher implementation
A transposition cipher that rearranges the message based on a keyword
"""


def encrypt(plaintext: str, keyword: str) -> str:
    """
    Encrypts plaintext using Columnar Transposition cipher
    
    Args:
        plaintext: The text to encrypt
        keyword: The keyword for column ordering
    
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
    
    clean_text = ''.join(c for c in plaintext if c.isalpha()).upper()
    
    cols = len(clean_keyword)
    rows = -(-len(clean_text) // cols)  # Ceiling division
    
    # Fill the grid
    grid = [['X' for _ in range(cols)] for _ in range(rows)]
    index = 0
    
    for i in range(rows):
        for j in range(cols):
            if index < len(clean_text):
                grid[i][j] = clean_text[index]
                index += 1
    
    # Determine column order based on keyword
    column_orders = []
    for i in range(cols):
        column_orders.append((i, clean_keyword[i].upper()))
    
    column_orders.sort(key=lambda x: x[1])
    
    # Read columns in order
    result = []
    for original_index, _ in column_orders:
        for i in range(rows):
            result.append(grid[i][original_index])
    
    return ''.join(result)


def decrypt(ciphertext: str, keyword: str) -> str:
    """
    Decrypts ciphertext using Columnar Transposition cipher
    
    Args:
        ciphertext: The text to decrypt
        keyword: The keyword for column ordering
    
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
    
    clean_text = ''.join(c for c in ciphertext if c.isalpha()).upper()
    
    cols = len(clean_keyword)
    rows = -(-len(clean_text) // cols)
    
    # Determine column order based on keyword
    column_orders = []
    for i in range(cols):
        column_orders.append((i, clean_keyword[i].upper()))
    
    column_orders.sort(key=lambda x: x[1])
    
    # Calculate column lengths
    col_lengths = [0] * cols
    base_length = len(clean_text) // cols
    extra = len(clean_text) % cols
    
    for i in range(cols):
        col_lengths[i] = base_length + (1 if i < extra else 0)
    
    # Fill columns from ciphertext
    columns = [[None for _ in range(rows)] for _ in range(cols)]
    index = 0
    
    for original_index, _ in column_orders:
        length = col_lengths[original_index]
        for i in range(length):
            if index < len(clean_text):
                columns[original_index][i] = clean_text[index]
                index += 1
    
    # Read grid row by row
    result = []
    for i in range(rows):
        for j in range(cols):
            if i < col_lengths[j]:
                result.append(columns[j][i])
    
    return ''.join(result)
