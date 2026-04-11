# Unit 1 — NFR Design Patterns

## Security Patterns

### Defense in Depth
Multiple security layers protect data:
1. **AES-GCM authentication tag**: Verifies ciphertext integrity and authenticity
2. **Argon2id key derivation**: Resists brute-force attacks on passwords
3. **Memory clearing**: Prevents sensitive data lingering in memory

### Fail-Closed Decryption
- On GCM auth tag verification failure: no plaintext is written to output
- `AesGcmEngine.decryptStream()` verifies auth tag before writing any decrypted data
- Throws `DecryptionException` on failure

### Secure Random Pattern
- All cryptographic randomness uses `java.security.SecureRandom`
- Covers: keys, salts, IVs
- No predictable or weak random sources

### Memory Safety Pattern
- `KeyManager.clearSensitiveBytes(byte[])` — zero-fills byte arrays
- `KeyManager.clearSensitiveChars(char[])` — zero-fills char arrays
- Applied after key derivation and encryption operations

---

## Performance Patterns

### Streaming I/O Pattern
- 64 KiB chunked processing avoids loading entire file into memory
- `InputStream` → `AesGcmEngine` → `OutputStream` pipeline
- Applicable to files of any size

### Single-Pass Encryption
- One read + one write per chunk (encrypt)
- Two reads per chunk (ciphertext + auth tag verify then decrypt)

---

## Reliability Patterns

### Stream Lifecycle Management
- `StreamProcessor` guarantees stream closure via try-with-resources
- Streams closed on success, failure, and exception paths
