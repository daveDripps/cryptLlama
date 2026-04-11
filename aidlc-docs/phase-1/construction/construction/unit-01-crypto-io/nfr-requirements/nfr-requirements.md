# Unit 1 — NFR Requirements

All non-functional requirements for Unit 1 were defined during the Requirements Analysis phase. This document consolidates them for reference.

## Security

### NFR-01: Encryption Standard
- **Requirement**: AES-256-GCM (Galois/Counter Mode) providing both confidentiality and integrity/authentication
- **Implementation**: `AesGcmEngine`
- **Verification**: GCM auth tag is verified on every decryption; no partial plaintext output on failure

### NFR-02: Key Derivation
- **Requirement**: Adaptive algorithm with sufficient iteration count; salt at least 16 bytes from cryptographically secure RNG
- **Implementation**: Argon2id via Bouncy Castle (`bcprov-jdk18on`)
- **Parameters**:
  - Memory cost: 64 MiB (65536 KiB)
  - Iterations: 3
  - Parallelism: 4
  - Salt: 16 bytes (SecureRandom)
- **Implementation**: `KeyManager.deriveKey()`

### NFR-03: Random Number Generation
- **Requirement**: All random values (keys, salts, IVs) from `java.security.SecureRandom`
- **Implementation**: `KeyManager` — `generateRandomKey()`, `generateSalt()`, `generateIv()`

### NFR-04: Memory Handling
- **Requirement**: Clear sensitive data (passwords, keys) from memory as soon as no longer needed; use `char[]` not `String` for passwords
- **Implementation**: `KeyManager.clearSensitiveBytes()`, `KeyManager.clearSensitiveChars()`
- **Rules**:
  - Password char[] zeroed immediately after key derivation
  - Derived key byte[] zeroed immediately after use
  - Generated key byte[] zeroed after encryption result is built

---

## Performance

### NFR-07: Streaming I/O
- **Requirement**: Handle file encryption/decryption efficiently using streaming I/O; avoid loading entire files into memory
- **Implementation**: 64 KiB (65536 bytes) chunked processing via `StreamProcessor` and `AesGcmEngine`
- **Rationale**: Balanced for memory usage vs I/O overhead for typical files

---

## N/A for Unit 1
- NFR-05 (Usability — plain language): Unit 3 (CLI layer)
- NFR-06 (Portability): Unit 0 (build.gradle)
- NFR-08 (Extensibility): Unit 2 (Service layer)
