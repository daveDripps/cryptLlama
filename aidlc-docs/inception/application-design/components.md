# LlamaCrypt — Components

## Design Decisions (Resolved)
| Decision | Choice |
|---|---|
| Java version | Java 21 LTS |
| Key Derivation Function | Argon2id (via Bouncy Castle) |
| Build tool | Gradle (Groovy DSL) |
| Package namespace | `com.llamacrypt` |
| File header versioning | Version byte included |

---

## Component Overview

LlamaCrypt is structured into four layers to support current CLI use and future GUI/web/mobile phases:

```
CLI Layer        → ArgumentParser, ConsoleIO
Service Layer    → EncryptionService, DecryptionService
Crypto Layer     → AesGcmEngine, KeyManager
I/O Layer        → FileFormatHandler, StreamProcessor
```

---

## Component 1 — Main

**Class**: `com.llamacrypt.Main`
**Layer**: Entry Point

**Purpose**: Application entry point. Bootstraps the application, wires together the service and CLI layers, and delegates execution based on the parsed operation mode.

**Responsibilities**:
- Receive raw command-line arguments from the JVM
- Instantiate and coordinate ArgumentParser, ConsoleIO, EncryptionService, and DecryptionService
- Route to the appropriate service based on parsed operation mode (ENCRYPT / DECRYPT)
- Pass results and errors to ConsoleIO for user-facing output
- Ensure the process exits with the correct exit code (0 = success, non-zero = failure)

**Interfaces**: None (top-level entry point)

---

## Component 2 — ArgumentParser

**Class**: `com.llamacrypt.cli.ArgumentParser`
**Layer**: CLI Layer

**Purpose**: Parses and validates command-line arguments, determines the operation mode from the source file extension, and collects the password if required interactively.

**Responsibilities**:
- Parse the `lcrypt [sourcefile] [password]` argument pattern
- Determine operation mode: ENCRYPT (any extension except `.lcrypt`) or DECRYPT (`.lcrypt` extension)
- Validate that the source file argument is present
- Package results into a `ParsedArgs` value object for consumption by Main
- Delegate interactive password collection to ConsoleIO when in DECRYPT mode and no password argument was provided

**Interfaces**:
- Input: `String[] args` (raw JVM arguments)
- Output: `ParsedArgs` value object

---

## Component 3 — ConsoleIO

**Class**: `com.llamacrypt.cli.ConsoleIO`
**Layer**: CLI Layer

**Purpose**: All user-facing console input and output. Provides plain-language messages, the interactive hidden-input password prompt, and the prominent auto-generated key warning.

**Responsibilities**:
- Print plain-language success messages including output file paths
- Print plain-language error messages without exposing internal details
- Print the auto-generated key warning (prefixed `⚠ WARNING:`) before the success message, with the key on its own clearly labelled line
- Read a password from the terminal with hidden input (characters not echoed)
- Trigger process exit with the correct exit code via `System.exit()`

**Interfaces**:
- Input: message strings, key strings, password prompts
- Output: console (stdout/stderr) and process exit codes

---

## Component 4 — AesGcmEngine

**Class**: `com.llamacrypt.crypto.AesGcmEngine`
**Layer**: Crypto Layer

**Purpose**: Low-level AES-256-GCM cipher operations. Performs authenticated encryption and decryption using the Java Cryptography Extension (JCE) with the GCM authentication tag providing both confidentiality and integrity.

**Responsibilities**:
- Initialise an AES-256-GCM cipher instance with the supplied key and IV/nonce
- Stream-encrypt plaintext data, producing ciphertext with an appended 128-bit GCM authentication tag
- Stream-decrypt ciphertext, verifying the GCM authentication tag before producing any plaintext output
- Raise a `DecryptionException` on authentication tag failure (wrong password or tampered file)
- Never load entire files into memory; use buffered streaming via JCE `CipherInputStream` / `CipherOutputStream`

**Interfaces**:
- Input: 256-bit key (`byte[]`), 12-byte IV (`byte[]`), source `InputStream`, destination `OutputStream`
- Output: encrypted/decrypted bytes written to the destination stream

---

## Component 5 — KeyManager

**Class**: `com.llamacrypt.crypto.KeyManager`
**Layer**: Crypto Layer

**Purpose**: All key material creation and management. Derives a 256-bit AES key from a password using Argon2id (via Bouncy Castle), generates cryptographically random keys and salts, and ensures sensitive data is cleared from memory after use.

**Responsibilities**:
- Derive a 256-bit AES key from a password and salt using Argon2id with hardened parameters
- Generate a cryptographically strong 256-bit random key when no password is provided (using `java.security.SecureRandom`)
- Generate a 16-byte random salt for Argon2id (using `SecureRandom`)
- Generate a 12-byte random IV/nonce for AES-GCM (using `SecureRandom`)
- Zero out sensitive `byte[]` and `char[]` arrays immediately after use (NFR-04)
- Expose Argon2id parameters (memory cost, iteration count, parallelism) so they can be stored in the file header for later decryption

**Interfaces**:
- Input: password (`char[]` — not `String` per NFR-04), salt (`byte[]`)
- Output: derived or generated key (`byte[]`), salt (`byte[]`), IV (`byte[]`)

---

## Component 6 — FileFormatHandler

**Class**: `com.llamacrypt.format.FileFormatHandler`
**Layer**: I/O Layer

**Purpose**: Serialises and deserialises the `.lcrypt` file header. Owns the definition of the binary header format and ensures all metadata required for decryption is correctly written at encryption time and correctly read back at decryption time.

**Responsibilities**:
- Write the structured binary header to an output stream: magic bytes (`LCRY`), format version (1 byte), Argon2id parameters (memory, iterations, parallelism), salt (16 bytes), IV/nonce (12 bytes)
- Read and parse the structured binary header from an input stream, validating the magic bytes and version
- Raise a `FormatException` if the header is missing, malformed, or contains an unrecognised version number
- Provide the parsed `LcryptHeader` value object to the service layer

**Interfaces**:
- Input: `OutputStream` (write) or `InputStream` (read)
- Output: `LcryptHeader` value object on read; header bytes written to stream on write

**Header Structure** (fixed layout):
```
Offset  Size  Field
0       4     Magic bytes ("LCRY")
4       1     Format version (0x01)
5       4     Argon2id memory cost (KiB, big-endian int)
9       4     Argon2id iteration count (big-endian int)
13      1     Argon2id parallelism
14      16    Salt
30      12    IV/nonce
--- total header: 42 bytes ---
[ciphertext + 16-byte GCM auth tag follows]
```

---

## Component 7 — StreamProcessor

**Class**: `com.llamacrypt.io.StreamProcessor`
**Layer**: I/O Layer

**Purpose**: Manages buffered file streaming for encryption and decryption operations. Abstracts file open/close/error handling so that service components work only with streams, not file-system paths.

**Responsibilities**:
- Open source and destination files as buffered streams
- Apply a caller-supplied `StreamOperation` (encrypt or decrypt) across the streams
- Handle `IOException` from file operations and translate to `ProcessingException`
- Ensure streams are closed in all exit paths (success and failure), releasing file handles and OS resources
- Support files of any size without loading into memory (NFR-07)

**Interfaces**:
- Input: source `Path`, destination `Path`, `StreamOperation` (functional interface)
- Output: none (side-effect: destination file written); raises `ProcessingException` on failure
