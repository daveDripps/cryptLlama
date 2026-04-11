# Requirements Clarification Questions

Please answer the following questions to help clarify the requirements for **cryptLlama**.
Answer each question by filling in the letter choice after the [Answer]: tag.
If none of the options match your needs, choose the last option (Other) and describe your preference.

---

## Question 1
What programming language should cryptLlama be built with?

A) Java (aligns with JetBrains/IntelliJ project structure already in place)
B) Python
C) JavaScript/TypeScript (Node.js)
D) Kotlin
X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 2
What type of user interface should the application have?

A) Command-line interface (CLI)
B) Desktop GUI application (e.g., JavaFX, Swing, Tkinter)
C) Web application (browser-based)
D) Library/API only (no UI, used by other programs)
X) Other (please describe after [Answer]: tag below)

[Answer]: A

## Question 3
What encryption algorithms should be supported?

A) AES (symmetric encryption - industry standard, fast)
B) RSA (asymmetric encryption - public/private key pairs)
C) Both AES and RSA
D) Simple ciphers only (Caesar, Vigenere - educational purpose)
X) Other (please describe after [Answer]: tag below)

[Answer]: X - AES-256-GCM specifically. Industry standard authenticated encryption providing both confidentiality and tamper detection.

## Question 4
What are the primary use cases for cryptLlama?

A) Encrypt/decrypt text messages or notes for personal use
B) Encrypt/decrypt files and documents
C) Secure communication between users (send encrypted messages)
D) Password/secrets management
X) Other (please describe after [Answer]: tag below)

[Answer]: B

## Question 5
How should encryption keys be managed?

A) User provides a password/passphrase that derives the key
B) Application generates and stores keys automatically
C) User provides their own encryption keys directly
D) Both password-based and direct key options
X) Other (please describe after [Answer]: tag below)

[Answer]: X - Password-based key derivation when user provides a password. When no password is provided, auto-generate a cryptographically strong random key and output it with a warning that the file will be impossible to decrypt without it.

## Question 6
What is the target audience and skill level?

A) Non-technical users who want simple encrypt/decrypt functionality
B) Developers who need a quick encryption utility
C) Students learning about cryptography
D) General audience (all skill levels)
X) Other (please describe after [Answer]: tag below)

[Answer]: D

## Question 7
Should encrypted output be portable/shareable?

A) Yes - output should be a text string (Base64 encoded) that can be copied/pasted
B) Yes - output should be saved to files
C) Both text strings and files
D) No - encryption/decryption happens in-place only
X) Other (please describe after [Answer]: tag below)

[Answer]: B - Encrypted file output with a custom extension (e.g. .cllama)

## Question 8: Security Extensions
Should security extension rules be enforced for this project?

A) Yes - enforce all SECURITY rules as blocking constraints (recommended for production-grade applications)
B) No - skip all SECURITY rules (suitable for PoCs, prototypes, and experimental projects)
X) Other (please describe after [Answer]: tag below)

[Answer]: A
