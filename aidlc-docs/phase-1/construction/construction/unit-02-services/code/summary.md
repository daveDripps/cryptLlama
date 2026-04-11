# Unit 2 — Service Layer

## Generated Files

### Service Layer
| File | Purpose |
|------|---------|
| `service/EncryptionService.java` | Encryption workflow orchestration |
| `service/DecryptionService.java` | Decryption workflow orchestration |
| `service/EncryptionResult.java` | Result value object |
| `service/DecryptionResult.java` | Result value object |
| `service/ServiceException.java` | Service-level exception |

### Tests
| File | Tests |
|------|-------|
| `service/EncryptionServiceTest.java` | Encryption with/without password, key generation |
| `service/DecryptionServiceTest.java` | Decryption, wrong password, tampered file |
| `service/EncryptionDecryptionRoundTripTest.java` | End-to-end encryption/decryption |

## Build Commands
```bash
./gradlew build        # compile + test + coverage
```
