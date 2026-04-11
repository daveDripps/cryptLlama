# Unit 1 — Tech Stack Decisions

## Recommended Developer Tools

### Required for Development
| Tool | Purpose | Installation |
|------|---------|--------------|
| Java 21 JDK | Compile and run | `sdk install java 21.0.2-tem` or [Adoptium](https://adoptium.net/) |
| Gradle 8.x | Build tool | `sdk install gradle 8.5` or [gradle.org](https://gradle.org/install/) |

### Recommended Tools (Full Suite)
| Tool | Purpose | Gradle Plugin |
|------|---------|---------------|
| **Checkstyle** | Java style checking | `checkstyle` |
| **SpotBugs** | Static bug detection | `spotbugs` |
| **JaCoCo** | Code coverage | `jacoco` |
| **Error Prone** | Static analysis for common bugs | `error-prone` |

### Installation Notes
- All tools run via Gradle — no separate installation needed
- Run with: `./gradlew check` (runs all checks)
- Individual tools:
  - `./gradlew checkstyleMain` — style check main sources
  - `./gradlew spotbugsMain` — bug detection on main sources
  - `./gradlew test` — runs tests with JaCoCo coverage
  - `./gradlew build` — full build including all checks

---

## Technology Choices

### Java 21 LTS
- **Choice**: Java 21 (selected during Requirements Analysis)
- **Rationale**: Best long-term support; virtual threads available for future phases
- **Location**: `build.gradle` (`sourceCompatibility`, `targetCompatibility`)

### Bouncy Castle `bcprov-jdk18on`
- **Choice**: Bouncy Castle for Argon2id (exact pinned version)
- **Version**: `1.78.1` (pinned in `build.gradle`)
- **Rationale**: Argon2id not available in standard Java crypto library
- **Location**: `dependencies` block in `build.gradle`

### Gradle (Groovy DSL)
- **Choice**: Gradle with Groovy DSL (selected during Application Design)
- **Rationale**: Flexible incremental builds; strong IntelliJ integration
- **Location**: `build.gradle`, `settings.gradle`

### Streaming Buffer Size: 64 KiB
- **Choice**: 64 KiB streaming buffer
- **Rationale**: Balances memory efficiency (not loading entire file) with I/O overhead reduction (fewer syscalls)
- **Location**: `StreamProcessor`, `AesGcmEngine`

### Key Encoding: Base64
- **Choice**: Base64 (URL-safe) for auto-generated key display
- **Rationale**: Shorter than hex (22 chars vs 32 chars for 16 bytes); URL-safe variant avoids shell escaping issues
- **Location**: Key display in `EncryptionResult` (Unit 2) and `ConsoleIO` (Unit 3)
