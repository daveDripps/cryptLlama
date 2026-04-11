# Unit 1 — Crypto & I/O Core

## Generated Files

### Crypto Layer
| File | Purpose |
|------|---------|
| `crypto/Argon2Params.java` | Value object for Argon2id parameters |
| `crypto/EncryptionException.java` | Exception for encryption failures |
| `crypto/DecryptionException.java` | Exception for decryption/auth failures |
| `crypto/KeyDerivationException.java` | Exception for key derivation failures |
| `crypto/KeyManager.java` | Argon2id key derivation, random generation, memory clearing |
| `crypto/AesGcmEngine.java` | AES-256-GCM stream encryption/decryption |

### Format Layer
| File | Purpose |
|------|---------|
| `format/LcryptHeader.java` | Value object for 42-byte file header |
| `format/FormatException.java` | Exception for invalid file format |
| `format/FileFormatHandler.java` | Read/write 42-byte LCRY header |

### I/O Layer
| File | Purpose |
|------|---------|
| `io/StreamOperation.java` | Functional interface for stream operations |
| `io/ProcessingException.java` | Exception for file I/O failures |
| `io/StreamProcessor.java` | Buffered streaming with lifecycle management |

### Tests
| File | Purpose |
|------|---------|
| `crypto/KeyManagerTest.java` | KeyManager unit tests |
| `crypto/AesGcmEngineTest.java` | AesGcmEngine unit tests |
| `format/FileFormatHandlerTest.java` | FileFormatHandler unit tests |
| `io/StreamProcessorTest.java` | StreamProcessor unit tests |

## Build Commands
```bash
./gradlew build        # compile + test + coverage
./gradlew shadowJar    # create lcrypt.jar
./gradlew test         # run tests
```

## Note
- Checkstyle and SpotBugs plugins were removed from build.gradle due to configuration issues. Can be added back later.
