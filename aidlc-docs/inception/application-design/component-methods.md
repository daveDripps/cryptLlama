# LlamaCrypt — Component Methods

> **Note**: This document defines method signatures and high-level purpose only.
> Detailed business logic, algorithm parameters, and business rules are defined later
> in Functional Design (CONSTRUCTION phase).

---

## Main — `com.llamacrypt.Main`

### `main(String[] args) : void`
**Visibility**: public static
**Purpose**: JVM entry point. Wires dependencies, parses arguments, routes to the appropriate service, and outputs results.
**Input**: Raw JVM argument array
**Output**: None (delegates exit code to ConsoleIO)
**Notes**: Catches all unchecked exceptions at the top level; any unhandled exception results in a generic error message and non-zero exit.

---

## ArgumentParser — `com.llamacrypt.cli.ArgumentParser`

### `parse(String[] args) : ParsedArgs`
**Visibility**: public
**Purpose**: Parses the raw argument array into a structured `ParsedArgs` value object. Determines operation mode (ENCRYPT / DECRYPT) from the source file extension.
**Input**: `String[] args`
**Output**: `ParsedArgs` — contains: `operationMode`, `sourcePath`, `password` (nullable `char[]`), `passwordProvided` flag
**Throws**: `InvalidArgumentException` if the argument array is empty or malformed
**Notes**: Detailed business rules for mode detection and password handling defined in Functional Design.

### `validate(ParsedArgs args) : void`
**Visibility**: public
**Purpose**: Validates the parsed arguments against file-system preconditions (file exists, file is readable).
**Input**: `ParsedArgs`
**Output**: None
**Throws**: `InvalidArgumentException` with a plain-language message for each validation failure

---

## ConsoleIO — `com.llamacrypt.cli.ConsoleIO`

### `printSuccess(String message) : void`
**Visibility**: public
**Purpose**: Writes a plain-language success message to stdout.
**Input**: Human-readable success string (e.g., `"File encrypted successfully: output.txt.lcrypt"`)
**Output**: None (console side-effect)

### `printError(String message) : void`
**Visibility**: public
**Purpose**: Writes a plain-language error message to stderr. No internal implementation details, class names, or stack traces.
**Input**: Human-readable error string
**Output**: None (console side-effect)

### `printAutoKeyWarning(String encodedKey) : void`
**Visibility**: public
**Purpose**: Displays the prominent auto-generated key warning followed by the key itself on its own labelled line. Warning is prefixed `⚠ WARNING:` and appears before the success message (US-03).
**Input**: Base64 or hex-encoded auto-generated key string
**Output**: None (console side-effect)
**Notes**: Warning format and exact wording defined in Functional Design.

### `readPasswordInteractively(String prompt) : char[]`
**Visibility**: public
**Purpose**: Prompts the user for a password with hidden input (characters not echoed). Uses `System.console()` for echo suppression.
**Input**: Prompt string shown to the user
**Output**: `char[]` — password entered by the user (caller is responsible for zeroing after use)
**Throws**: `ConsoleException` if no system console is available (e.g., running in a piped/IDE context)

### `exit(int code) : void`
**Visibility**: public
**Purpose**: Terminates the process with the given exit code. Exit code 0 = success; non-zero = failure (US-05).
**Input**: Exit code integer
**Output**: None (process terminates)

---

## AesGcmEngine — `com.llamacrypt.crypto.AesGcmEngine`

### `encryptStream(InputStream plaintext, OutputStream ciphertext, byte[] key, byte[] iv) : void`
**Visibility**: public
**Purpose**: Encrypts the plaintext stream using AES-256-GCM, writing ciphertext (with appended 128-bit authentication tag) to the output stream.
**Input**: Source `InputStream`, destination `OutputStream`, 256-bit key `byte[]`, 12-byte IV `byte[]`
**Output**: None (ciphertext written to stream as side-effect)
**Throws**: `EncryptionException` on cipher failure

### `decryptStream(InputStream ciphertext, OutputStream plaintext, byte[] key, byte[] iv) : void`
**Visibility**: public
**Purpose**: Decrypts the ciphertext stream using AES-256-GCM, verifying the 128-bit authentication tag before producing any output. Fails closed — no partial plaintext is written on auth failure.
**Input**: Source `InputStream` (ciphertext + auth tag), destination `OutputStream`, 256-bit key `byte[]`, 12-byte IV `byte[]`
**Output**: None (plaintext written to stream as side-effect)
**Throws**: `DecryptionException` on authentication tag failure (wrong password or tampered data); also thrown on cipher failure

---

## KeyManager — `com.llamacrypt.crypto.KeyManager`

### `deriveKey(char[] password, byte[] salt, Argon2Params params) : byte[]`
**Visibility**: public
**Purpose**: Derives a 256-bit AES key from the supplied password and salt using Argon2id (Bouncy Castle). Parameters control memory cost, iteration count, and parallelism.
**Input**: Password as `char[]`, 16-byte salt, `Argon2Params` value object
**Output**: 32-byte derived key
**Throws**: `KeyDerivationException` on failure
**Notes**: Exact Argon2id parameter values (memory, iterations, parallelism) defined in Functional Design (NFR-02 compliance).

### `generateRandomKey() : byte[]`
**Visibility**: public
**Purpose**: Generates a cryptographically strong 256-bit (32-byte) random key using `SecureRandom`.
**Input**: None
**Output**: 32-byte random key

### `generateSalt() : byte[]`
**Visibility**: public
**Purpose**: Generates a 16-byte cryptographically random salt for Argon2id using `SecureRandom` (NFR-02, NFR-03).
**Input**: None
**Output**: 16-byte salt

### `generateIv() : byte[]`
**Visibility**: public
**Purpose**: Generates a 12-byte cryptographically random IV/nonce for AES-GCM using `SecureRandom` (NFR-03).
**Input**: None
**Output**: 12-byte IV

### `clearSensitiveBytes(byte[] data) : void`
**Visibility**: public
**Purpose**: Overwrites a byte array with zeros to clear sensitive key material from memory (NFR-04).
**Input**: `byte[]` to zero
**Output**: None (in-place mutation)

### `clearSensitiveChars(char[] data) : void`
**Visibility**: public
**Purpose**: Overwrites a char array with zero characters to clear password data from memory (NFR-04).
**Input**: `char[]` to zero
**Output**: None (in-place mutation)

---

## FileFormatHandler — `com.llamacrypt.format.FileFormatHandler`

### `writeHeader(OutputStream out, LcryptHeader header) : void`
**Visibility**: public
**Purpose**: Serialises a `LcryptHeader` value object to the output stream as a fixed 42-byte binary header.
**Input**: `OutputStream`, `LcryptHeader` — contains magic, version, Argon2id params, salt, IV
**Output**: None (header bytes written to stream as side-effect)
**Throws**: `FormatException` on write failure

### `readHeader(InputStream in) : LcryptHeader`
**Visibility**: public
**Purpose**: Reads and deserialises the 42-byte binary header from the input stream, validating magic bytes and format version.
**Input**: `InputStream` positioned at the start of a `.lcrypt` file
**Output**: `LcryptHeader` value object
**Throws**: `FormatException` if magic bytes are invalid, version is unrecognised, or the stream ends prematurely

---

## StreamProcessor — `com.llamacrypt.io.StreamProcessor`

### `process(Path source, Path destination, StreamOperation operation) : void`
**Visibility**: public
**Purpose**: Opens buffered streams for the source and destination paths, applies the supplied `StreamOperation`, and ensures streams are closed in all exit paths (success and failure).
**Input**: Source `Path`, destination `Path`, `StreamOperation` functional interface (`InputStream` → `OutputStream` → `void`)
**Output**: None (destination file written as side-effect)
**Throws**: `ProcessingException` wrapping any `IOException` from file open, read, write, or close

---

## Value Objects and Supporting Types

| Type | Package | Purpose |
|---|---|---|
| `ParsedArgs` | `com.llamacrypt.cli` | Immutable result of argument parsing |
| `LcryptHeader` | `com.llamacrypt.format` | Immutable parsed/built file header |
| `Argon2Params` | `com.llamacrypt.crypto` | Argon2id tuning parameters (memory, iterations, parallelism) |
| `OperationMode` | `com.llamacrypt.cli` | Enum: ENCRYPT / DECRYPT |
| `StreamOperation` | `com.llamacrypt.io` | Functional interface for stream transform |
| `EncryptionException` | `com.llamacrypt.crypto` | Cipher failure during encryption |
| `DecryptionException` | `com.llamacrypt.crypto` | Auth tag failure or cipher failure during decryption |
| `KeyDerivationException` | `com.llamacrypt.crypto` | Key derivation failure |
| `FormatException` | `com.llamacrypt.format` | Invalid or unrecognised file header |
| `ProcessingException` | `com.llamacrypt.io` | File I/O failure during stream processing |
| `InvalidArgumentException` | `com.llamacrypt.cli` | Invalid or missing CLI arguments |
| `ConsoleException` | `com.llamacrypt.cli` | Console unavailable for interactive input |
