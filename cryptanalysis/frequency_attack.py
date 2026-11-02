"""
Frequency analysis attack implementation
"""

from analysis.frequency import letter_frequency, most_frequent_letters, get_english_frequency_reference


def frequency_analysis(ciphertext: str) -> str:
    """
    Performs frequency analysis on ciphertext
    
    Args:
        ciphertext: The ciphertext to analyze
    
    Returns:
        Formatted string with analysis results
    """
    freq = letter_frequency(ciphertext)
    total = sum(freq.values())
    
    output = "FREQUENCY ANALYSIS\n"
    output += "===================\n\n"
    
    output += "Letter Frequency in Ciphertext:\n"
    for letter in [chr(ord('A') + i) for i in range(26)]:
        if letter in freq:
            pct = (freq[letter] / total) * 100 if total > 0 else 0
            output += f"  {letter}: {freq[letter]} ({pct:.2f}%)\n"
    
    output += "\nMost Frequent Letters:\n"
    top_letters = most_frequent_letters(ciphertext, 5)
    for i, (letter, count, pct) in enumerate(top_letters):
        output += f"  {i+1}. {letter} ({pct:.2f}%)\n"
    
    output += "\nAnalysis:\n"
    output += "In English, the most common letters are E, T, A, O, I, N, S, H, R.\n"
    output += "If the ciphertext shows a different distribution, it may indicate:\n"
    output += "  - A substitution cipher (letters are mapped to different letters)\n"
    output += "  - A transposition cipher (letter frequencies are preserved)\n"
    output += "  - A polyalphabetic cipher (frequencies are flattened)\n"
    
    if top_letters:
        most_common = top_letters[0][0]
        output += f"\nFor Caesar cipher: If '{most_common}' is most frequent, "
        output += "it likely maps to 'E'.\n"
        output += "This suggests a shift based on the difference between letters.\n"
    
    return output
