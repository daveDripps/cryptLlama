# Unit 1 — NFR Design: Logical Components

## Component Architecture

```
InputStream (file)
      |
      v
StreamProcessor
      |
      v
AesGcmEngine (encrypt/decrypt in 64 KiB chunks)
      |
      v
OutputStream (file)
```

## No External Infrastructure
Unit 1 has no:
- Databases
- Message queues
- Caches
- External services
- Network dependencies

All components use only:
- Java standard library (`java.io`, `java.nio`)
- JCE (`javax.crypto`)
- Bouncy Castle (`org.bouncycastle.crypto`) for Argon2id

## Component Responsibilities (NFR View)

| Component | NFRs Addressed |
|-----------|---------------|
| `AesGcmEngine` | NFR-01 (AES-256-GCM), NFR-07 (streaming) |
| `KeyManager` | NFR-02 (Argon2id), NFR-03 (SecureRandom), NFR-04 (memory clearing) |
| `FileFormatHandler` | NFR-01 (GCM auth tag in header) |
| `StreamProcessor` | NFR-07 (streaming I/O), reliability (stream closure) |
