"""
Brute-force attack implementations
"""

from algorithms.classical.caesar import brute_force as caesar_brute_force
from algorithms.classical.affine import brute_force as affine_brute_force


def caesar_attack(ciphertext: str) -> list:
    """
    Performs brute-force attack on Caesar cipher
    
    Args:
        ciphertext: The ciphertext to attack
    
    Returns:
        List of all possible plaintexts (26 possibilities)
    """
    return caesar_brute_force(ciphertext)


def affine_attack(ciphertext: str) -> list:
    """
    Performs brute-force attack on Affine cipher
    
    Args:
        ciphertext: The ciphertext to attack
    
    Returns:
        List of all possible plaintexts (312 possibilities)
    """
    return affine_brute_force(ciphertext)


def format_caesar_results(ciphertext: str) -> str:
    """
    Formats Caesar brute-force results for display
    
    Args:
        ciphertext: The ciphertext that was attacked
    
    Returns:
        Formatted string with all possibilities
    """
    results = caesar_attack(ciphertext)
    
    output = "CAESAR BRUTE-FORCE ATTACK\n"
    output += "========================\n"
    output += "Testing all 26 possible shifts...\n\n"
    
    for i, plaintext in enumerate(results):
        output += f"Shift {i:2d}: {plaintext}\n"
    
    output += "\nAnalysis:\n"
    output += "Caesar cipher has a key space of only 26 possible keys.\n"
    output += "This makes it extremely vulnerable to brute-force attacks.\n"
    output += "The correct plaintext can be identified by looking for\n"
    output += "meaningful English words or by using frequency analysis.\n"
    
    return output


def format_affine_results(ciphertext: str) -> str:
    """
    Formats Affine brute-force results for display
    
    Args:
        ciphertext: The ciphertext that was attacked
    
    Returns:
        Formatted string with top results
    """
    results = affine_attack(ciphertext)
    
    output = "AFFINE BRUTE-FORCE ATTACK\n"
    output += "=========================\n"
    output += "Testing all valid key combinations...\n"
    output += "(12 valid 'a' keys × 26 'b' keys = 312 possibilities)\n\n"
    
    from algorithms.classical.affine import get_valid_key_a_values
    valid_a = get_valid_key_a_values()
    
    count = 0
    for i, result in enumerate(results):
        if not result.startswith("Error"):
            a = valid_a[i // 26]
            b = i % 26
            output += f"a={a}, b={b}: {result}\n"
            count += 1
            
            if count >= 50:
                output += "... (showing first 50 results)\n"
                break
    
    output += "\nAnalysis:\n"
    output += "Affine cipher has a key space of 312 possible keys.\n"
    output += "Key 'a' must be coprime with 26 (valid values: 1,3,5,7,9,11,15,17,19,21,23,25).\n"
    output += "While larger than Caesar, it's still vulnerable to brute-force.\n"
    
    return output
