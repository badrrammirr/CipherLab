"""
Statistical analysis module for frequency distribution
"""

from collections import Counter


def character_frequency(text: str) -> dict:
    """
    Calculates character frequency distribution
    
    Args:
        text: Text to analyze
    
    Returns:
        Dictionary mapping characters to their counts
    """
    return dict(Counter(text))


def letter_frequency(text: str) -> dict:
    """
    Calculates letter frequency distribution (A-Z only)
    
    Args:
        text: Text to analyze
    
    Returns:
        Dictionary mapping letters (A-Z) to their counts
    """
    text = text.upper()
    letters = [c for c in text if c.isalpha() and c <= 'Z']
    return dict(Counter(letters))


def letter_frequency_percentage(text: str) -> dict:
    """
    Calculates letter frequency as percentages (A-Z only)
    
    Args:
        text: Text to analyze
    
    Returns:
        Dictionary mapping letters (A-Z) to their percentage
    """
    freq = letter_frequency(text)
    total = sum(freq.values())
    
    if total == 0:
        return {chr(ord('A') + i): 0.0 for i in range(26)}
    
    return {letter: (count / total) * 100 for letter, count in freq.items()}


def most_frequent_letters(text: str, n: int = 5) -> list:
    """
    Gets the n most frequent letters
    
    Args:
        text: Text to analyze
        n: Number of top letters to return
    
    Returns:
        List of tuples (letter, count, percentage)
    """
    freq = letter_frequency(text)
    total = sum(freq.values())
    
    if total == 0:
        return []
    
    sorted_freq = sorted(freq.items(), key=lambda x: x[1], reverse=True)
    return [(letter, count, (count / total) * 100) for letter, count in sorted_freq[:n]]


def compare_frequencies(plaintext: str, ciphertext: str) -> dict:
    """
    Compares letter frequencies between plaintext and ciphertext
    
    Args:
        plaintext: Original plaintext
        ciphertext: Encrypted ciphertext
    
    Returns:
        Dictionary with comparison data
    """
    plain_freq = letter_frequency_percentage(plaintext)
    cipher_freq = letter_frequency_percentage(ciphertext)
    
    comparison = {}
    for letter in [chr(ord('A') + i) for i in range(26)]:
        plain_pct = plain_freq.get(letter, 0.0)
        cipher_pct = cipher_freq.get(letter, 0.0)
        diff = abs(plain_pct - cipher_pct)
        comparison[letter] = {
            'plaintext_pct': plain_pct,
            'ciphertext_pct': cipher_pct,
            'difference': diff
        }
    
    return comparison


def get_english_frequency_reference() -> dict:
    """
    Returns standard English letter frequency reference
    
    Returns:
        Dictionary mapping letters to their expected frequency percentage
    """
    return {
        'E': 12.7, 'T': 9.1, 'A': 8.2, 'O': 7.5, 'I': 7.0,
        'N': 6.7, 'S': 6.3, 'H': 6.1, 'R': 6.0, 'D': 4.3,
        'L': 4.0, 'C': 2.8, 'U': 2.8, 'M': 2.4, 'W': 2.4,
        'F': 2.2, 'G': 2.0, 'Y': 2.0, 'P': 1.9, 'B': 1.5,
        'V': 1.0, 'K': 0.8, 'J': 0.15, 'X': 0.15, 'Q': 0.10,
        'Z': 0.07
    }
