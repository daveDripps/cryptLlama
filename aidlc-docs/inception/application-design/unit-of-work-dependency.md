# LlamaCrypt — Unit of Work Dependencies

## Unit Dependency Overview

LlamaCrypt has four development units. Unit 0 is a hard prerequisite for all others —
no feature code is written until the repository, CI, and stub build are in place.
Units 1–3 then follow a strict linear dependency chain.

```
Unit 0               Unit 1                Unit 2                Unit 3
Repo & CI/CD  --->   Crypto & I/O   --->   Service Layer  --->   CLI & Entry
(no deps)            (depends on 0)        (depends on 1)        (depends on 2)
```

---

## Unit Dependency Matrix

| Unit | Depends On | Required Before |
|---|---|---|
| Unit 0 — Repo & CI/CD Setup | None | Units 1, 2, 3 |
| Unit 1 — Crypto & I/O Core | Unit 0 (green build exists) | Units 2, 3 |
| Unit 2 — Service Layer | Unit 1 | Unit 3 |
| Unit 3 — CLI & Entry Point | Unit 2 (transitively Unit 1) | Nothing (final unit) |

---

## Branch & PR Dependency

| Unit | Branch | Merges Into | PR Required |
|---|---|---|---|
| Unit 0 | `main` (direct initial commit) | — | No (bootstrapping) |
| Unit 1 | `feature/unit-01-crypto-io` | `main` | Yes — CI must pass |
| Unit 2 | `feature/unit-02-services` | `main` | Yes — CI must pass |
| Unit 3 | `feature/unit-03-cli` | `main` | Yes — CI must pass |

Each feature branch is created from `main` after the previous unit's PR is merged.

---

## Component-Level Cross-Unit Dependencies

### Unit 1 depends on Unit 0

Unit 1 development starts from the stub created in Unit 0. The `build.gradle` already
declares Bouncy Castle and Java 21 — Unit 1 adds source files only.

### Unit 2 depends on Unit 1

| Unit 2 Component | Uses from Unit 1 |
|---|---|
| `EncryptionService` | `KeyManager`, `AesGcmEngine`, `FileFormatHandler`, `StreamProcessor` |
| `DecryptionService` | `KeyManager`, `AesGcmEngine`, `FileFormatHandler`, `StreamProcessor` |

### Unit 3 depends on Unit 2

| Unit 3 Component | Uses from Unit 2 |
|---|---|
| `Main` | `EncryptionService`, `DecryptionService` |
| `ArgumentParser` | None directly (produces `ParsedArgs` consumed by Main) |
| `ConsoleIO` | None directly (called by Main and ArgumentParser) |

---

## Construction Sequence

```
[Unit 0 — Repo & CI/CD Setup]
  Create GitHub repo → push stub → set branch protection → green CI
      |
      | PR: none (direct to main)
      v
[Unit 1 — Crypto & I/O]
  Branch: feature/unit-01-crypto-io
  Build + Unit Test (AesGcmEngine, KeyManager, FileFormatHandler, StreamProcessor)
      |
      | PR: feature/unit-01-crypto-io → main (CI required)
      v
[Unit 2 — Service Layer]
  Branch: feature/unit-02-services (from main after Unit 1 merge)
  Build + Unit Test + Integration Test (EncryptionService, DecryptionService)
      |
      | PR: feature/unit-02-services → main (CI required)
      v
[Unit 3 — CLI & Entry Point]
  Branch: feature/unit-03-cli (from main after Unit 2 merge)
  Build + Unit Test + End-to-End Test (all 5 user stories from CLI)
      |
      | PR: feature/unit-03-cli → main (CI required)
      v
[Build and Test Stage]
  Full build on main, combined test run, fat JAR packaging
```

---

## External Dependency

Bouncy Castle (`bcprov-jdk18on`) is declared in Unit 0's `build.gradle`.
It is available to all subsequent units since all share a single Gradle project.
