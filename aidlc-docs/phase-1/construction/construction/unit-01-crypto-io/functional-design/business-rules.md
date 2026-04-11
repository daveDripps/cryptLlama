# Unit 1 — Functional Design: Business Rules

## Encryption Rules
1. Source file must exist and be readable (checked by CLI, not crypto layer)
2. Output file is created in same directory as source
3. Source file is never modified or deleted
4. IV is generated fresh for each encryption operation
5. Salt is generated fresh for each encryption operation
6. AES-GCM auth tag is appended to ciphertext (16 bytes)

## Decryption Rules
1. Input file must be a valid `.lcrypt` file with correct magic bytes
2. Format version must be recognized (0x01)
3. Salt and IV are read from header
4. Key is derived using Argon2id with header salt and stored parameters
5. GCM auth tag is verified before any plaintext is written
6. On auth tag failure: no output file created, throw DecryptionException
7. Output filename = input filename with `.lcrypt` extension removed

## Memory Clearing Rules
1. Password char[] is zeroed immediately after key derivation
2. Derived key byte[] is zeroed immediately after use
3. Generated key byte[] is zeroed after encryption result is built
4. Salt and IV are not sensitive but may be cleared after use

## Exception Rules
1. EncryptionException: cipher initialization or encryption I/O failure
2. DecryptionException: auth tag failure OR cipher failure
3. KeyDerivationException: Argon2id initialization or execution failure
4. FormatException: invalid magic bytes, unrecognized version, or truncated header
5. ProcessingException: file I/O failure (wrapped IOException)
6. All exceptions contain plain-language messages only (no class names, no stack traces)
