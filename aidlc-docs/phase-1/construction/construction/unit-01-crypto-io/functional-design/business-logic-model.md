# Unit 1 — Functional Design: Business Logic Model

## Crypto Layer

### AesGcmEngine
- **Encrypt flow**: Reads plaintext in 64 KiB chunks → AES-256-GCM encrypts → writes ciphertext + auth tag to output stream
- **Decrypt flow**: Reads ciphertext in 64 KiB chunks → AES-256-GCM decrypts → verifies auth tag per chunk → writes plaintext only after successful verification
- **GCM auth tag**: 128-bit (16 bytes), appended to ciphertext per GCM specification
- **IV**: 96-bit (12 bytes) nonce, randomly generated per encryption

### KeyManager
- **Key derivation**: Argon2id with:
  - Memory cost: 64 MiB (65536 KiB)
  - Iterations: 3
  - Parallelism: 4
- **Random key generation**: 256-bit (32 bytes) using SecureRandom
- **Salt generation**: 128-bit (16 bytes) using SecureRandom
- **IV generation**: 96-bit (12 bytes) using SecureRandom
- **Memory clearing**: Zero-fill byte[]/char[] after use (NFR-04)
- **Key encoding**: Base64 for display (URL-safe, 22 characters for 16-byte salt/key)

---

## I/O Layer

### StreamProcessor
- **Buffer size**: 64 KiB (65536 bytes)
- **Pattern**: Opens source/destination streams → applies StreamOperation → guarantees stream closure on all exit paths
- **No in-memory full file**: Chunks processed sequentially

### FileFormatHandler
- **Header size**: 42 bytes (fixed)
- **Format**: Binary big-endian

```
Offset  Size  Field
0       4     Magic bytes "LCRY"
4       1     Format version (0x01)
5       4     Argon2id memory cost (KiB, big-endian int)
9       4     Argon2id iteration count (big-endian int)
13      1     Argon2id parallelism
14      16    Salt
30      12    IV/nonce
[42 bytes total; ciphertext + 16-byte GCM auth tag follow]
```

---

## Value Objects

### Argon2Params
- `memoryCostKiB`: int (65536)
- `iterations`: int (3)
- `parallelism`: int (4)

### LcryptHeader
- `magic`: String ("LCRY")
- `version`: byte (0x01)
- `argon2Params`: Argon2Params
- `salt`: byte[16]
- `iv`: byte[12]

### StreamOperation
- Functional interface: `(InputStream, OutputStream) → void`
