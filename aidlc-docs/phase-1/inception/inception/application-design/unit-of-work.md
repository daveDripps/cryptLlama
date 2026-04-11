# LlamaCrypt — Units of Work

## Decomposition Strategy
**Approach**: Four units, built in sequence.
Unit 0 establishes the repository, CI/CD, and a green stub build before any
feature code is written. Units 1–3 are built bottom-up by layer, with each unit
fully designed, coded, and tested before the next begins. Each of Units 1–3
ends with a PR from its feature branch into `main`.

**Deployment model**: Single JAR — Units 1–3 are development/construction boundaries
only, not deployment boundaries.

---

## Unit 0 — Repository & CI/CD Setup

**ID**: `unit-00-repo-setup`
**Layer**: Infrastructure / DevOps
**Construction order**: First — completed before any application code is written
**Branch**: Initial commit pushed directly to `main` (branch protection applied after)
**PR at end**: None — Unit 0 establishes `main` as the baseline

### Deliverables
| # | Deliverable | Detail |
|---|---|---|
| 1 | GitHub repository | `LlamaCrypt`, public, on Dave's account |
| 2 | Stub Java project | `Main.java` (empty main method that exits cleanly), `build.gradle`, `settings.gradle`, `.gitignore` |
| 3 | GitHub Actions workflow | `.github/workflows/build.yml` — compiles and runs tests on every push and PR |
| 4 | Main branch protection | PRs required; CI build must pass before merge |
| 5 | Green build confirmed | First CI run passes on `main` |

### Stub Project Structure
```
LlamaCrypt/                      ← workspace root (also local IntelliJ project)
  build.gradle                   ← Gradle Groovy DSL; Java 21, Bouncy Castle, shadow JAR
  settings.gradle                ← rootProject.name = 'LlamaCrypt'
  .gitignore                     ← Java / Gradle / IntelliJ ignores
  .github/
    workflows/
      build.yml                  ← build + test on push/PR
  src/
    main/java/com/llamacrypt/
      Main.java                  ← empty: public static void main(String[] args) {}
    test/java/com/llamacrypt/
      PlaceholderTest.java       ← trivially passing test so CI has something to run
```

### Branch & PR Strategy (applies from Unit 1 onwards)
- Development for each unit happens on a dedicated feature branch:
  - Unit 1: `feature/unit-01-crypto-io`
  - Unit 2: `feature/unit-02-services`
  - Unit 3: `feature/unit-03-cli`
- At the end of each unit, a PR is opened from the feature branch → `main`
- CI must pass and the PR must be reviewed before merging

---

## Unit 1 — Crypto & I/O Core

**ID**: `unit-01-crypto-io`
**Layer**: Crypto Layer + I/O Layer
**Construction order**: Second (depends on Unit 0 green build)
**Branch**: `feature/unit-01-crypto-io` → PR to `main` at completion

### Components
| Component | Class | Responsibility |
|---|---|---|
| AesGcmEngine | `com.llamacrypt.crypto.AesGcmEngine` | AES-256-GCM stream encryption and decryption |
| KeyManager | `com.llamacrypt.crypto.KeyManager` | Argon2id key derivation, random key/salt/IV generation, memory clearing |
| FileFormatHandler | `com.llamacrypt.format.FileFormatHandler` | `.lcrypt` binary header serialisation/deserialisation |
| StreamProcessor | `com.llamacrypt.io.StreamProcessor` | Buffered file streaming with stream lifecycle management |

### Value Objects & Exceptions (defined in this unit)
- `com.llamacrypt.crypto.Argon2Params`
- `com.llamacrypt.format.LcryptHeader`
- `com.llamacrypt.io.StreamOperation`
- `com.llamacrypt.crypto.EncryptionException`
- `com.llamacrypt.crypto.DecryptionException`
- `com.llamacrypt.crypto.KeyDerivationException`
- `com.llamacrypt.format.FormatException`
- `com.llamacrypt.io.ProcessingException`

### External Dependency
- Bouncy Castle `bcprov-jdk18on` (pinned exact version in `build.gradle`)

### What this unit delivers
A fully testable, standalone cryptographic and file I/O library. All encryption,
decryption, key derivation, random generation, header read/write, and streaming
operations are implemented and unit-tested.

### Code Organisation
```
src/main/java/com/llamacrypt/
  crypto/
    AesGcmEngine.java
    KeyManager.java
    Argon2Params.java
    EncryptionException.java
    DecryptionException.java
    KeyDerivationException.java
  format/
    FileFormatHandler.java
    LcryptHeader.java
    FormatException.java
  io/
    StreamProcessor.java
    StreamOperation.java
    ProcessingException.java

src/test/java/com/llamacrypt/
  crypto/
    AesGcmEngineTest.java
    KeyManagerTest.java
  format/
    FileFormatHandlerTest.java
  io/
    StreamProcessorTest.java
```

---

## Unit 2 — Service Layer

**ID**: `unit-02-services`
**Layer**: Service Layer
**Construction order**: Third (depends on Unit 1)
**Branch**: `feature/unit-02-services` → PR to `main` at completion

### Components
| Component | Class | Responsibility |
|---|---|---|
| EncryptionService | `com.llamacrypt.service.EncryptionService` | Full encryption workflow orchestration |
| DecryptionService | `com.llamacrypt.service.DecryptionService` | Full decryption workflow orchestration |

### Value Objects & Exceptions (defined in this unit)
- `com.llamacrypt.service.EncryptionResult`
- `com.llamacrypt.service.DecryptionResult`
- `com.llamacrypt.service.ServiceException`

### What this unit delivers
Fully testable end-to-end encryption and decryption workflows. All scenarios
(password, no-password, wrong-password, tampered file) are covered by integration
tests against real files — no CLI wiring yet.

### Code Organisation
```
src/main/java/com/llamacrypt/
  service/
    EncryptionService.java
    DecryptionService.java
    EncryptionResult.java
    DecryptionResult.java
    ServiceException.java

src/test/java/com/llamacrypt/
  service/
    EncryptionServiceTest.java
    DecryptionServiceTest.java
    EncryptionDecryptionRoundTripTest.java
```

---

## Unit 3 — CLI & Entry Point

**ID**: `unit-03-cli`
**Layer**: CLI Layer + Entry Point
**Construction order**: Fourth (depends on Unit 2)
**Branch**: `feature/unit-03-cli` → PR to `main` at completion

### Components
| Component | Class | Responsibility |
|---|---|---|
| ArgumentParser | `com.llamacrypt.cli.ArgumentParser` | CLI argument parsing and validation |
| ConsoleIO | `com.llamacrypt.cli.ConsoleIO` | All user-facing console output and interactive input |
| Main | `com.llamacrypt.Main` | JVM entry point, dependency wiring, routing |

### Value Objects & Exceptions (defined in this unit)
- `com.llamacrypt.cli.ParsedArgs`
- `com.llamacrypt.cli.OperationMode`
- `com.llamacrypt.cli.InvalidArgumentException`
- `com.llamacrypt.cli.ConsoleException`

### What this unit delivers
The complete, runnable application. End-to-end acceptance tests validate all five
user stories from the command line (`lcrypt`). The final distributable fat JAR
is produced at the end of this unit.

### Code Organisation
```
src/main/java/com/llamacrypt/
  Main.java
  cli/
    ArgumentParser.java
    ConsoleIO.java
    ParsedArgs.java
    OperationMode.java
    InvalidArgumentException.java
    ConsoleException.java

src/test/java/com/llamacrypt/
  cli/
    ArgumentParserTest.java
  EndToEndEncryptTest.java
  EndToEndDecryptTest.java
```

---

## Build Configuration

**Build file**: `build.gradle` (Groovy DSL) at workspace root
**Main class**: `com.llamacrypt.Main`
**CLI command**: `lcrypt`
**Output**: Single executable fat JAR via Gradle Shadow plugin
