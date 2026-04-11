# Unit 0 — Repo & CI/CD Setup

## Generated Files

| File | Purpose |
|------|---------|
| `build.gradle` | Gradle build config — Java 21, Bouncy Castle, Shadow JAR |
| `settings.gradle` | Root project name |
| `.gitignore` | Java/Gradle/IDE ignores |
| `src/main/java/com/llamacrypt/Main.java` | Stub entry point |
| `src/test/java/com/llamacrypt/PlaceholderTest.java` | Trivially passing test |
| `.github/workflows/build.yml` | GitHub Actions CI |

## Build Commands
```bash
./gradlew build        # compile + test
./gradlew shadowJar     # create lcrypt.jar
```
