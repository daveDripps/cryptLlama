# LlamaCrypt — Application Design (Consolidated)

**Phase**: INCEPTION  
**Stage**: Application Design  
**Project**: LlamaCrypt — Java CLI file encryption tool  
**Date**: 2026-04-11

---

## 1. Design Decisions

| Decision | Choice | Rationale |
|---|---|---|
| Java version | Java 21 LTS | Best long-term support; virtual threads available for future phases |
| Key Derivation Function | Argon2id (Bouncy Castle) | Strongest resistance to GPU/ASIC brute-force attacks (NFR-02) |
| Build tool | Gradle (Groovy DSL) | Flexible incremental builds; strong IntelliJ integration |
| Package namespace | `com.llamacrypt` | Conventional company-style; extensible to future sub-projects |
| File header versioning | Version byte included | Enables format migrations without breaking existing `.lcrypt` files |

---

## 2. Architecture Overview

LlamaCrypt is structured in four layers. The service layer is deliberately decoupled from the CLI layer so that future GUI, web, and mobile frontends can reuse the same encryption/decryption logic without modification (NFR-08).

```
+----------------------------------------------------------+
|                      CLI LAYER                           |
|         ArgumentParser          ConsoleIO                |
+----------------------------------------------------------+
                        |
                        v
+----------------------------------------------------------+
|                    SERVICE LAYER                         |
|         EncryptionService       DecryptionService        |
+----------------------------------------------------------+
                        |
                        v
+----------------------------------------------------------+
|          CRYPTO LAYER              I/O LAYER             |
|    AesGcmEngine   KeyManager    FileFormatHandler        |
|                                  StreamProcessor         |
+----------------------------------------------------------+
```

---

## 3. Components

### 3.1 Main — `com.llamacrypt.Main`
Entry point. Wires dependencies, routes to the appropriate service, and passes results to ConsoleIO. Catches all unchecked exceptions at the top level for safe error reporting.

### 3.2 ArgumentParser — `com.llamacrypt.cli.ArgumentParser`
Parses the `lcrypt [sourcefile] [password]` pattern. Determines ENCRYPT or DECRYPT mode from the source file extension (`.lcrypt` = DECRYPT; anything else = ENCRYPT). Delegates interactive password collection to ConsoleIO when needed.

### 3.3 ConsoleIO — `com.llamacrypt.cli.ConsoleIO`
All user-facing input and output. Prints plain-language success and error messages, the prominent auto-generated key warning (⚠ WARNING:), and the key on its own labelled line. Reads passwords interactively with hidden input via `System.console()`. Manages process exit codes.

### 3.4 AesGcmEngine — `com.llamacrypt.crypto.AesGcmEngine`
Low-level AES-256-GCM cipher operations using JCE. Streams plaintext to ciphertext (with appended GCM auth tag) on encryption. Verifies the GCM auth tag before producing any plaintext on decryption — fails closed on authentication failure.

### 3.5 KeyManager — `com.llamacrypt.crypto.KeyManager`
All key material operations. Derives a 256-bit AES key using Argon2id (Bouncy Castle). Generates cryptographically random keys, salts, and IVs using `SecureRandom`. Zeros all sensitive `byte[]` and `char[]` arrays after use (NFR-04).

### 3.6 FileFormatHandler — `com.llamacrypt.format.FileFormatHandler`
Owns the `.lcrypt` binary file header format. Writes and reads: magic bytes (`LCRY`), format version (1 byte), Argon2id parameters, salt (16 bytes), and IV (12 bytes) — 42 bytes total. Raises `FormatException` on invalid or unrecognised headers.

**Header layout**:
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

### 3.7 StreamProcessor — `com.llamacrypt.io.StreamProcessor`
Manages buffered file streams. Accepts a `StreamOperation` functional interface, opens source and destination files, applies the operation, and ensures streams are closed in all exit paths. Supports files of any size without loading into memory (NFR-07).

---

## 4. Services

### 4.1 EncryptionService — `com.llamacrypt.service.EncryptionService`
Orchestrates the full encryption workflow:
1. Decide key strategy (derive from password, or auto-generate)
2. Generate salt and IV
3. Build and write the `.lcrypt` header
4. Stream-encrypt source file via AesGcmEngine
5. Return output path and optional auto-generated key string

### 4.2 DecryptionService — `com.llamacrypt.service.DecryptionService`
Orchestrates the full decryption workflow:
1. Read and validate the `.lcrypt` header
2. Derive the key from password + header salt
3. Stream-decrypt file body via AesGcmEngine (GCM auth tag verified)
4. On auth failure: raise `ServiceException("Incorrect password or the file is corrupted")`
5. Return output path

---

## 5. Key Method Signatures

```java
// Entry point
com.llamacrypt.Main
  public static void main(String[] args)

// CLI
com.llamacrypt.cli.ArgumentParser
  public ParsedArgs parse(String[] args) throws InvalidArgumentException
  public void validate(ParsedArgs args) throws InvalidArgumentException

com.llamacrypt.cli.ConsoleIO
  public void printSuccess(String message)
  public void printError(String message)
  public void printAutoKeyWarning(String encodedKey)
  public char[] readPasswordInteractively(String prompt) throws ConsoleException
  public void exit(int code)

// Crypto
com.llamacrypt.crypto.AesGcmEngine
  public void encryptStream(InputStream in, OutputStream out, byte[] key, byte[] iv) throws EncryptionException
  public void decryptStream(InputStream in, OutputStream out, byte[] key, byte[] iv) throws DecryptionException

com.llamacrypt.crypto.KeyManager
  public byte[] deriveKey(char[] password, byte[] salt, Argon2Params params) throws KeyDerivationException
  public byte[] generateRandomKey()
  public byte[] generateSalt()
  public byte[] generateIv()
  public void clearSensitiveBytes(byte[] data)
  public void clearSensitiveChars(char[] data)

// I/O
com.llamacrypt.format.FileFormatHandler
  public void writeHeader(OutputStream out, LcryptHeader header) throws FormatException
  public LcryptHeader readHeader(InputStream in) throws FormatException

com.llamacrypt.io.StreamProcessor
  public void process(Path source, Path destination, StreamOperation operation) throws ProcessingException

// Services
com.llamacrypt.service.EncryptionService
  public EncryptionResult encrypt(Path sourceFile, char[] password) throws ServiceException

com.llamacrypt.service.DecryptionService
  public DecryptionResult decrypt(Path sourceFile, char[] password) throws ServiceException
```

---

## 6. Component Dependencies

| Component | Depends On |
|---|---|
| `Main` | `ArgumentParser`, `ConsoleIO`, `EncryptionService`, `DecryptionService` |
| `ArgumentParser` | `ConsoleIO` |
| `ConsoleIO` | None |
| `EncryptionService` | `KeyManager`, `AesGcmEngine`, `FileFormatHandler`, `StreamProcessor` |
| `DecryptionService` | `KeyManager`, `AesGcmEngine`, `FileFormatHandler`, `StreamProcessor` |
| `AesGcmEngine` | None (JCE only) |
| `KeyManager` | None (JCE + Bouncy Castle) |
| `FileFormatHandler` | None |
| `StreamProcessor` | None |

---

## 7. External Dependencies

| Library | Purpose |
|---|---|
| Bouncy Castle `bcprov-jdk18on` (pinned exact version) | Argon2id key derivation |
| Java 21 JDK standard library | AES-GCM, SecureRandom, file I/O, System.console() |

---

## 8. Security Extension Compliance (SECURITY-11 — Application Design)

**SECURITY-11: Secure Design Principles** — COMPLIANT

- **Separation of concerns**: Security-critical logic (key derivation in `KeyManager`, cipher operations in `AesGcmEngine`) is isolated in dedicated components — not scattered across the codebase.
- **Defense in depth**: Multiple controls are layered: input validation (ArgumentParser) + AES-GCM authentication tag (AesGcmEngine) + Argon2id (KeyManager) + memory clearing (KeyManager).
- **Rate limiting**: N/A — CLI tool; no public-facing endpoint. No rate limiting is applicable at application design level.
- **Business logic abuse**: Misuse cases considered — wrong password returns generic error without leaking whether the password or the file is the problem; auto-generated key warning is mandatory and prominent (US-03).

All other SECURITY rules (SECURITY-01 through SECURITY-10, SECURITY-12 through SECURITY-15) are evaluated as **N/A** at Application Design stage — they apply at Code Generation and Build and Test stages where implementation details, authentication mechanisms, and dependency lock files are produced.

---

## 9. Artifact Locations

| Artifact | Path |
|---|---|
| Components | `aidlc-docs/inception/application-design/components.md` |
| Component Methods | `aidlc-docs/inception/application-design/component-methods.md` |
| Services | `aidlc-docs/inception/application-design/services.md` |
| Component Dependencies | `aidlc-docs/inception/application-design/component-dependency.md` |
| This document (consolidated) | `aidlc-docs/inception/application-design/application-design.md` |
