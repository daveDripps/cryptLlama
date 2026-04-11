# Unit 1 - Crypto & I/O Core: Code Generation Plan

## Unit Context
- **Unit ID**: `unit-01-crypto-io`
- **Layer**: Crypto Layer + I/O Layer
- **Branch**: `feature/unit-01-crypto-io` → PR to `main` at completion
- **Stories**: US-01, US-02, US-04 (core pipelines)

## Dependencies
- Unit 0 (Gradle setup, Bouncy Castle dependency)

## Step-by-Step Generation Plan

### Step 1: Create Package Directories
- [x] Create `src/main/java/com/llamacrypt/crypto/`
- [x] Create `src/main/java/com/llamacrypt/format/`
- [x] Create `src/main/java/com/llamacrypt/io/`
- [x] Create `src/test/java/com/llamacrypt/crypto/`
- [x] Create `src/test/java/com/llamacrypt/format/`
- [x] Create `src/test/java/com/llamacrypt/io/`

### Step 2: Crypto Layer — Value Objects & Exceptions
- [x] Create `crypto/Argon2Params.java` — value object
- [x] Create `crypto/EncryptionException.java`
- [x] Create `crypto/DecryptionException.java`
- [x] Create `crypto/KeyDerivationException.java`

### Step 3: Crypto Layer — KeyManager
- [x] Create `crypto/KeyManager.java` — Argon2id derivation, random generation, memory clearing

### Step 4: Crypto Layer — AesGcmEngine
- [x] Create `crypto/AesGcmEngine.java` — AES-256-GCM stream encrypt/decrypt

### Step 5: Format Layer — Value Objects & Exceptions
- [x] Create `format/LcryptHeader.java` — header value object
- [x] Create `format/FormatException.java`

### Step 6: Format Layer — FileFormatHandler
- [x] Create `format/FileFormatHandler.java` — 42-byte header read/write

### Step 7: I/O Layer — Value Objects & Exceptions
- [x] Create `io/StreamOperation.java` — functional interface
- [x] Create `io/ProcessingException.java`

### Step 8: I/O Layer — StreamProcessor
- [x] Create `io/StreamProcessor.java` — buffered streaming with lifecycle management

### Step 9: Unit Tests — KeyManager
- [x] Create `crypto/KeyManagerTest.java`

### Step 10: Unit Tests — AesGcmEngine
- [x] Create `crypto/AesGcmEngineTest.java`

### Step 11: Unit Tests — FileFormatHandler
- [x] Create `format/FileFormatHandlerTest.java`

### Step 12: Unit Tests — StreamProcessor
- [x] Create `io/StreamProcessorTest.java`

### Step 13: Documentation Summary
- [x] Create markdown summary in `aidlc-docs/construction/unit-01-crypto-io/code/`

### Step 14: Push Branch and Create PR
- [x] Push `feature/unit-01-crypto-io` branch to remote
- [x] Create PR from `feature/unit-01-crypto-io` → `main`
- [x] Provide PR link for review

## Project Structure
```
src/main/java/com/llamacrypt/
  crypto/
    Argon2Params.java
    AesGcmEngine.java
    DecryptionException.java
    EncryptionException.java
    KeyDerivationException.java
    KeyManager.java
  format/
    FileFormatHandler.java
    FormatException.java
    LcryptHeader.java
  io/
    ProcessingException.java
    StreamOperation.java
    StreamProcessor.java

src/test/java/com/llamacrypt/
  crypto/
    AesGcmEngineTest.java
    KeyManagerTest.java
  format/
    FileFormatHandlerTest.java
  io/
    StreamProcessorTest.java
```

## Build Commands
```bash
./gradlew build        # compile + test
./gradlew check       # style + bug checks
```
