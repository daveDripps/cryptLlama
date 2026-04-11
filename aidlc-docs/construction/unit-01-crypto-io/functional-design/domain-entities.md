# Unit 1 — Functional Design: Domain Entities

## Exceptions

### EncryptionException (com.llamacrypt.crypto)
- Thrown when AES-256-GCM encryption fails
- Message: plain-language only (no class names)

### DecryptionException (com.llamacrypt.crypto)
- Thrown when AES-256-GCM decryption OR auth tag verification fails
- Message: "Incorrect password or the file is corrupted" (from service layer)
- No partial plaintext output on failure

### KeyDerivationException (com.llamacrypt.crypto)
- Thrown when Argon2id key derivation fails
- Message: plain-language only

### FormatException (com.llamacrypt.format)
- Thrown on invalid magic bytes, unrecognized version, or truncated header
- Message: plain-language only

### ProcessingException (com.llamacrypt.io)
- Wraps IOException from file operations
- Message: plain-language only

---

## Value Objects

### Argon2Params (com.llamacrypt.crypto)
| Field | Type | Value |
|-------|------|-------|
| memoryCostKiB | int | 65536 |
| iterations | int | 3 |
| parallelism | int | 4 |

### LcryptHeader (com.llamacrypt.format)
| Field | Type | Notes |
|-------|------|-------|
| magic | String | "LCRY" |
| version | byte | 0x01 |
| argon2Params | Argon2Params | |
| salt | byte[16] | |
| iv | byte[12] | |

### StreamOperation (com.llamacrypt.io)
- Functional interface: `(InputStream in, OutputStream out) → void`
- Implemented by AesGcmEngine for encrypt/decrypt operations

---

## Main Components

### AesGcmEngine
- **Package**: com.llamacrypt.crypto
- **Methods**:
  - `encryptStream(InputStream in, OutputStream out, byte[] key, byte[] iv)` → void
  - `decryptStream(InputStream in, OutputStream out, byte[] key, byte[] iv)` → void

### KeyManager
- **Package**: com.llamacrypt.crypto
- **Methods**:
  - `deriveKey(char[] password, byte[] salt, Argon2Params params)` → byte[32]
  - `generateRandomKey()` → byte[32]
  - `generateSalt()` → byte[16]
  - `generateIv()` → byte[12]
  - `clearSensitiveBytes(byte[] data)` → void
  - `clearSensitiveChars(char[] data)` → void

### FileFormatHandler
- **Package**: com.llamacrypt.format
- **Methods**:
  - `writeHeader(OutputStream out, LcryptHeader header)` → void
  - `readHeader(InputStream in)` → LcryptHeader

### StreamProcessor
- **Package**: com.llamacrypt.io
- **Methods**:
  - `process(Path source, Path destination, StreamOperation operation)` → void
