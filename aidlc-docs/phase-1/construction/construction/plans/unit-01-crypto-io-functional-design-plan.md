# Unit 1 - Crypto & I/O Core: Functional Design Plan

## Unit Context
- **Unit ID**: `unit-01-crypto-io`
- **Layer**: Crypto Layer + I/O Layer
- **Stories**: US-01, US-02, US-04 (core pipelines)
- **Components**: AesGcmEngine, KeyManager, FileFormatHandler, StreamProcessor

## Already Designed (No Work Needed)
- File format header layout (42 bytes: magic `LCRY`, version, Argon2id params, salt, IV) — in `component-methods.md`
- Component interfaces and method signatures — in `component-methods.md`
- Component responsibilities and dependencies — in `component-dependency.md`
- Service orchestration flows — in `component-dependency.md`

## Functional Design Work Required

### Step 1: Argon2id Parameters
- [x] Define Argon2id memory cost (KiB), iterations, and parallelism
- [x] Document rationale for chosen values

### Step 2: Buffer Size Strategy
- [x] Define streaming buffer size for StreamProcessor
- [x] Document rationale (memory efficiency vs I/O overhead)

### Step 3: Key Encoding Format
- [x] Define format for auto-generated key display (hex vs Base64)
- [x] Document rationale (usability vs length)

### Step 4: Error Handling Specifications
- [x] Document exact exception types thrown per failure scenario
- [x] Confirm failure messages (plain-language) for each exception type
- [x] Document GCM auth tag failure handling (no partial plaintext output)

## Questions Requiring User Input

**[Q1]** Argon2id parameters: The design says these will be defined here. Reasonable defaults for a CLI tool are:
- Memory cost: 64 MiB (65536 KiB)
- Iterations: 3
- Parallelism: 4

Do you want these values, or do you have specific requirements for key derivation strength?

**[Q2]** Buffer size: For streaming encryption/decryption, I propose a 64 KiB buffer. This balances memory usage with I/O efficiency for typical files. Acceptable?

**[Q3]** Key encoding: Auto-generated keys will be displayed as Base64 (22 characters for 16 bytes of entropy — URL-safe). Alternative is hex (32 characters). Which format for display?

**[Q4]** GCM auth tag handling: On auth tag verification failure, the design says "no partial plaintext output." Should I:
- A) Stream in 64 KiB chunks and discard as each chunk fails (more complex)
- B) Process in memory-constrained chunks and only write after auth verification passes on each chunk (simpler)
