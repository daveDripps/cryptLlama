# Unit 1 - Crypto & I/O Core: NFR Design Plan

## Unit Context
- **Unit ID**: `unit-01-crypto-io`
- **Layer**: Crypto Layer + I/O Layer

## NFR Design Status: Pre-Determined
All NFR design decisions for this unit are already specified in the functional design and NFR requirements.

| NFR Pattern | Implementation |
|-------------|----------------|
| Defense in Depth | AES-GCM auth tag + Argon2id + memory clearing |
| Fail-Closed Decryption | No partial plaintext on auth failure |
| Secure Random | SecureRandom for all cryptographic randomness |
| Memory Safety | Zero-fill char[]/byte[] after use |

## No Additional NFR Design Work Required
All security, performance, and reliability patterns are defined in functional design artifacts.
