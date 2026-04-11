# LlamaCrypt Requirements Document

## Intent Analysis Summary

- **User Request**: Build a basic, easy-to-use text/file encryption application called LlamaCrypt
- **Request Type**: New Project (Greenfield)
- **Scope Estimate**: Multiple Components (CLI interface, encryption engine, key management, file I/O)
- **Complexity Estimate**: Moderate (standard encryption patterns, but requires secure implementation)
- **Requirements Depth**: Standard

---

## 1. Functional Requirements

### FR-01: File Encryption
The application shall encrypt any file provided as input using AES-256-GCM authenticated encryption. The encrypted output shall be written to a new file with a `.lcrypt` extension appended to the original filename (e.g., `document.txt` becomes `document.txt.lcrypt`).

### FR-02: File Decryption
The application shall decrypt any `.lcrypt` file back to its original format. The decrypted output shall restore the original filename by removing the `.lcrypt` extension.

### FR-03: Command-Line Interface
The application shall provide a CLI with the following usage pattern:

```
lcrypt [sourcefile] [password]
```

Where `sourcefile` is required and `password` is optional. The application shall automatically detect whether to encrypt or decrypt based on the file extension (`.lcrypt` = decrypt, anything else = encrypt).

### FR-04: Password-Based Key Derivation
When a password is provided, the application shall derive the AES-256 encryption key using a secure key derivation function (e.g., PBKDF2 or Argon2) with a randomly generated salt. The salt shall be stored in the encrypted file header.

### FR-05: Auto-Generated Key (No Password Provided)
When no password is provided, the application shall generate a cryptographically strong random key. The application shall output the generated key/password to the console along with a prominent warning message stating that the file will be impossible to decrypt without this key in the future.

### FR-06: Interactive Password Prompt
When decrypting a file and no password is provided on the command line, the application shall prompt the user for the password interactively with hidden input (characters not echoed to the terminal).

### FR-07: Encrypted File Format
The encrypted file shall contain a structured header with metadata required for decryption, including: format identifier/magic bytes, version number, salt, IV/nonce, and authentication tag. This allows forward compatibility as the application evolves.

### FR-08: Encryption/Decryption Feedback
The application shall provide clear console output indicating success or failure of operations, including file paths for encrypted/decrypted output. Error messages shall be user-friendly and not expose internal implementation details.

---

## 2. Non-Functional Requirements

### NFR-01: Security - Encryption Standard
The application shall use AES-256-GCM (Galois/Counter Mode) providing both confidentiality and integrity/authentication of encrypted data.

### NFR-02: Security - Key Derivation
Password-based key derivation shall use an adaptive algorithm with sufficient iteration count to resist brute-force attacks. The salt shall be at least 16 bytes, generated from a cryptographically secure random source.

### NFR-03: Security - Random Number Generation
All random values (keys, salts, IVs/nonces) shall be generated using `java.security.SecureRandom` or equivalent cryptographically secure RNG.

### NFR-04: Security - Memory Handling
Sensitive data (passwords, keys) shall be cleared from memory as soon as they are no longer needed. Use `char[]` instead of `String` for password handling where possible.

### NFR-05: Usability - Target Audience
The application shall be accessible to users of all technical skill levels. Error messages and warnings shall use plain language. The auto-generated key warning (FR-05) shall be impossible to miss.

### NFR-06: Portability - Platform Support
As a Java application, LlamaCrypt shall run on any platform with a JDK (Windows, macOS, Linux). The target JDK version shall be determined during Application Design.

### NFR-07: Performance
The application shall handle file encryption/decryption efficiently using streaming I/O for large files, avoiding loading entire files into memory.

### NFR-08: Extensibility
The architecture shall be designed to support future additions: desktop GUI (JavaFX), web version, mobile version, and additional encryption algorithms (e.g., RSA, ChaCha20-Poly1305).

---

## 3. Constraints

### C-01: Programming Language
Java, running within the existing JetBrains/IntelliJ project structure.

### C-02: Interface (Phase 1)
CLI only. Desktop, web, and mobile interfaces are future phases.

### C-03: Algorithm (Phase 1)
AES-256-GCM only. Additional algorithms are future enhancements.

### C-04: Security Compliance
All AIDLC Security Extension rules are enforced as blocking constraints throughout development.

---

## 4. Out of Scope (Phase 1)

- Desktop GUI (JavaFX) - future phase
- Web application - future phase
- Mobile application - future phase
- RSA or other asymmetric encryption - future phase
- Text string encryption (non-file) - future phase
- Key/secrets management features - future phase
- Multi-user or shared encryption scenarios - future phase
