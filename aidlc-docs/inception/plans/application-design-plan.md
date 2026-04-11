# Application Design Plan — cryptLlama

## Context Summary
- **Project**: cryptLlama — Java CLI file encryption tool (Greenfield)
- **Algorithm**: AES-256-GCM
- **Key management**: PBKDF2 or Argon2 (to be confirmed below)
- **Interface**: CLI only (Phase 1)
- **Extensibility**: Must support future GUI, web, and mobile phases
- **Security extension**: ENABLED (blocking constraints)

---

## Plan Steps

- [x] Collect and resolve design decisions (questions below)
- [x] Generate `aidlc-docs/inception/application-design/components.md`
- [x] Generate `aidlc-docs/inception/application-design/component-methods.md`
- [x] Generate `aidlc-docs/inception/application-design/services.md`
- [x] Generate `aidlc-docs/inception/application-design/component-dependency.md`
- [x] Generate `aidlc-docs/inception/application-design/application-design.md` (consolidated)
- [x] Validate design completeness and consistency
- [x] Verify Security Extension compliance (SECURITY-11 compliant; others N/A at design stage)
- [x] Update `aidlc-docs/aidlc-state.md` to mark stage complete

---

## Design Questions

Please answer each question by filling in the letter after the `[Answer]:` tag.
If none of the options fit, choose the "Other" option and describe your preference after the tag.
Let me know when you are done.

---

## Question 1
Which Java LTS version should cryptLlama target?
(NFR-06 requires this to be decided at Application Design.)

A) Java 11 (LTS — widely deployed, older environments)
B) Java 17 (LTS — strong adoption, modern language features, still widely supported)
C) Java 21 (LTS — latest long-term release, virtual threads, best long-term choice)
D) Other (please describe after [Answer]: tag below)

[Answer]: c

---

## Question 2
Which key derivation function should be used for password-based encryption?
(Requirements list both PBKDF2 and Argon2 as candidates.)

A) PBKDF2WithHmacSHA256 — built into the Java standard library, no extra dependencies, widely accepted
B) Argon2id — stronger against GPU/ASIC attacks, but requires a third-party library (e.g., Bouncy Castle)
C) Other (please describe after [Answer]: tag below)

[Answer]: B

---

## Question 3
Which build tool should be used for the project?

A) Maven — conventional for Java, excellent IDE support, simple XML configuration
B) Gradle (Groovy DSL) — more flexible, faster incremental builds
C) Gradle (Kotlin DSL) — same as above but with type-safe build scripts
D) Other (please describe after [Answer]: tag below)

[Answer]: B

---

## Question 4
How should the Java package namespace be structured?

A) `com.cryptllama` — top-level company-style namespace (e.g. `com.cryptllama.cli`, `com.cryptllama.crypto`)
B) `io.cryptllama` — common open-source style (e.g. `io.cryptllama.cli`, `io.cryptllama.crypto`)
C) `cryptllama` — simple flat namespace (e.g. `cryptllama.cli`, `cryptllama.crypto`)
D) Other (please describe after [Answer]: tag below)

[Answer]: a

---

## Question 5
Should the encrypted file header include a version field to support future format migrations?
(FR-07 mandates magic bytes, salt, IV, auth tag. A version byte adds forward-compatibility.)

A) Yes — include a 1-byte format version in the header (recommended for extensibility)
B) No — keep the header minimal; versioning can be added later if needed
C) Other (please describe after [Answer]: tag below)

[Answer]: A