"""
Known-plaintext attack demonstration
"""

from algorithms.classical.caesar import encrypt as caesar_encrypt


def known_plaintext_attack(known_plaintext: str, known_ciphertext: str, target_ciphertext: str = None) -> str:
    """
    Demonstrates known-plaintext attack
    
    Args:
        known_plaintext: Known plaintext
        known_ciphertext: Known ciphertext
        target_ciphertext: Optional target ciphertext to decrypt using discovered key
    
    Returns:
        Formatted string with attack results
    """
    output = "KNOWN-PLAINTEXT ATTACK\n"
    output += "=======================\n\n"
    output += f"Known Plaintext: {known_plaintext}\n"
    output += f"Known Ciphertext: {known_ciphertext}\n\n"
    
    # Try to determine if it's Caesar
    if len(known_plaintext) >= 1 and len(known_ciphertext) >= 1:
        p = known_plaintext[0].upper()
        c = known_ciphertext[0].upper()
        
        if p.isalpha() and c.isalpha():
            shift = (ord(c) - ord(p) + 26) % 26
            output += f"Possible Caesar Shift: {shift}\n"
            output += f"Verification: {caesar_encrypt(known_plaintext, shift)}\n"
            
            if caesar_encrypt(known_plaintext, shift).upper() == known_ciphertext.upper():
                output += f"✓ Caesar cipher confirmed with shift {shift}\n"
                
                if target_ciphertext:
                    from algorithms.classical.caesar import decrypt as caesar_decrypt
                    decrypted = caesar_decrypt(target_ciphertext, shift)
                    output += f"\nTarget Ciphertext Decrypted: {decrypted}\n"
    
    output += "\nAnalysis:\n"
    output += "Known-plaintext attacks exploit the relationship between\n"
    output += "plaintext and ciphertext to determine the key.\n"
    output += "\nFor classical ciphers:\n"
    output += "  - Caesar: One known pair reveals the shift\n"
    output += "  - Affine: Two known pairs can solve the equations\n"
    output += "  - Vigenère: Known plaintext can reveal the key length\n"
    output += "  - Substitution: Known pairs build the substitution table\n"
    output += "\nModern ciphers (AES, RSA) are designed to resist\n"
    output += "known-plaintext attacks through diffusion and confusion.\n"
    
    return output
