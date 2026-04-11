# Unit of Work Plan — cryptLlama

## Context Summary
- **Project**: cryptLlama — Java 21 / Gradle monolith (single deployable JAR)
- **Components**: 7 components across 4 layers (CLI, Service, Crypto, I/O)
- **User stories**: 5 stories (US-01 to US-05) across 3 journeys

## Plan Steps

- [x] Collect and resolve decomposition decisions (question below)
- [x] Generate `aidlc-docs/inception/application-design/unit-of-work.md`
- [x] Generate `aidlc-docs/inception/application-design/unit-of-work-dependency.md`
- [x] Generate `aidlc-docs/inception/application-design/unit-of-work-story-map.md`
- [x] Validate unit boundaries — all stories assigned, dependencies clear
- [ ] Update `aidlc-docs/aidlc-state.md` to mark stage complete (pending user approval)

---

## Context for the Question Below

cryptLlama is a single deployable JAR — there is no question about deployment units.
The question is how to **sequence the development work** so that each unit can be
built and tested before the next one depends on it.

The 7 components fall naturally into layers with clear dependencies:

```
Layer 1 — Crypto & I/O (no dependencies on other layers)
  - AesGcmEngine, KeyManager, FileFormatHandler, StreamProcessor

Layer 2 — Services (depends on Layer 1)
  - EncryptionService, DecryptionService

Layer 3 — CLI & Entry (depends on Layer 2)
  - ArgumentParser, ConsoleIO, Main
```

---

## Design Question

Please answer the question below by filling in the letter after the `[Answer]:` tag.
Let me know when you are done.

---

## Question 1
How should the development work be decomposed into units of work?

A) **Three units (bottom-up by layer)** — Unit 1: Crypto & I/O layer (AesGcmEngine, KeyManager,
   FileFormatHandler, StreamProcessor); Unit 2: Service layer (EncryptionService,
   DecryptionService); Unit 3: CLI & Entry (ArgumentParser, ConsoleIO, Main).
   Each unit is fully designed, coded, and tested before the next begins.
   *Best for: structured delivery, clear test boundaries, security-critical core verified first.*

B) **Two units (core + integration)** — Unit 1: All crypto, I/O, and service logic
   (everything except CLI); Unit 2: CLI layer and end-to-end wiring.
   *Best for: slightly faster delivery while still isolating CLI from the core.*

C) **Single unit (whole application)** — All components designed and coded together
   in one pass. *Best for: smallest project where layer separation adds overhead.*

D) Other (please describe after [Answer]: tag below)

[Answer]: a