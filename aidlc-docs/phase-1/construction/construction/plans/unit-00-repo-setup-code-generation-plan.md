# Unit 0 - Repo & CI/CD Setup: Code Generation Plan

## Unit Context
- **Unit ID**: `unit-00-repo-setup`
- **Layer**: Infrastructure / DevOps
- **Construction Order**: First (before any application code)
- **Branch**: Direct commit to `main` (no PR)

## Stories Implemented
- Infrastructure setup (no user stories - this is pure setup)

## Dependencies
- None (first unit)

## Step-by-Step Generation Plan

### Step 1: Project Structure Setup
- [x] Create directory structure:
  - `src/main/java/com/llamacrypt/`
  - `src/test/java/com/llamacrypt/`

### Step 2: Gradle Configuration Files
- [x] Create `build.gradle` — Java 21, Bouncy Castle (pinned version), Shadow JAR plugin
- [x] Create `settings.gradle` — `rootProject.name = 'LlamaCrypt'`

### Step 3: Git Ignore
- [x] Create `.gitignore` — Java/Gradle/IntelliJ ignores

### Step 4: Stub Main Class
- [x] Create `src/main/java/com/llamacrypt/Main.java` — empty main method that exits cleanly

### Step 5: Stub Test
- [x] Create `src/test/java/com/llamacrypt/PlaceholderTest.java` — trivially passing test

### Step 6: GitHub Workflow
- [x] Create `.github/workflows/build.yml` — compiles and runs tests on push/PR

### Step 7: Documentation Summary
- [x] Create markdown summary in `aidlc-docs/construction/unit-00-repo-setup/code/`

## Stub Project Structure
```
LlamaCrypt/
  build.gradle
  settings.gradle
  .gitignore
  .github/
    workflows/
      build.yml
  src/
    main/java/com/llamacrypt/
      Main.java
    test/java/com/llamacrypt/
      PlaceholderTest.java
```

## Build Commands (for verification)
```bash
./gradlew build        # compile + test
./gradlew jar          # create distributable
```
