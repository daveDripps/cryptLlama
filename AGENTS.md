# AGENTS.md — LlamaCrypt

## Project
**LlamaCrypt** — Java 21 CLI file encryption app using AES-256-GCM + Argon2id.

## Stack
- **Language**: Java 17
- **Build**: Gradle (Groovy DSL) — `.gradle` files in project root
- **External dep**: `bcprov-jdk18on` (Bouncy Castle, exact pinned version) for Argon2id
- **CLI command**: `lcrypt` (`java -jar lcrypt.jar [sourcefile] [password]`)

## Architecture
- **Package**: `com.llamacrypt`
- **Layers** (CLI → Service → Crypto/IO):
  - `cli`: `ArgumentParser`, `ConsoleIO`
  - `service`: `EncryptionService`, `DecryptionService`
  - `crypto`: `AesGcmEngine`, `KeyManager`
  - `format`: `FileFormatHandler`
  - `io`: `StreamProcessor`
- **Service layer is framework-agnostic** — reusable for future GUI/web/mobile frontends
- **Entry point**: `com.llamacrypt.Main.main(String[])`

## File Locations
- **Source code**: Workspace root (`src/main/java/`, `src/test/java/` after Unit 0)
- **Documentation**: `aidlc-docs/` (NEVER put source code here)
- **Design docs**: `aidlc-docs/inception/application-design/` (application-design.md, components.md, etc.)

## Workflow
- Follows **AI-DLC adaptive workflow** (`.AiDLC/aidlc-rules/aws-aidlc-rules/core-workflow.md`)
- Stages have approval gates — DO NOT skip presenting completion messages
- Audit log: `aidlc-docs/audit.md` — append only, never overwrite
- State tracking: `aidlc-docs/aidlc-state.md`
- Security Baseline extension is **ENFORCED** (blocking) — all SECURITY rules apply at code/build stages

## Units
| Unit | Branch | Content |
|---|---|---|
| 0 | `main` | Repo setup, Gradle, CI |
| 1 | `feature/unit-01-crypto-io` | Crypto & I/O core |
| 2 | `feature/unit-02-services` | Service layer |
| 3 | `feature/unit-03-cli` | CLI & entry point |

## Build Commands (after Unit 0)
```bash
./gradlew build        # compile + test
./gradlew test         # run tests
./gradlew jar          # create distributable
./gradlew check        # run all checks (checkstyle + spotbugs + tests)
./gradlew checkstyleMain    # style check main sources
./gradlew spotbugsMain      # bug detection on main sources
```

## Recommended Tools
| Tool | Purpose | Run |
|------|---------|-----|
| Checkstyle | Java style checking | `./gradlew checkstyleMain` |
| SpotBugs | Static bug detection | `./gradlew spotbugsMain` |
| JaCoCo | Code coverage | Included in `./gradlew test` |
| Error Prone | Static analysis | Included in `./gradlew build` |

## Required Setup
- Java 17 JDK (e.g., [Adoptium](https://adoptium.net/))
- Gradle 8.x (or use wrapper: `./gradlew`)

## Security
- Use `char[]` for passwords (not `String`)
- Always clear sensitive data: `KeyManager.clearSensitiveBytes()`, `KeyManager.clearSensitiveChars()`
- AES-GCM fails closed on auth tag failure — no partial plaintext output
- Error messages must be plain-language only — no class names or stack traces
