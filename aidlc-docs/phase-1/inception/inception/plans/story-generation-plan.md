# Story Generation Plan — cryptLlama

## Plan Overview
This plan converts the approved cryptLlama requirements into user-centred stories with acceptance criteria.
All [Answer]: tags must be completed before generation begins.

---

## PART 1: Planning Questions

Please fill in all [Answer]: tags below, then let me know when you're done.

---

### Question 1
Which story breakdown approach should be used for cryptLlama?

A) **User Journey-Based** — Stories follow the user's workflow from start to finish (open terminal → run command → get result)
B) **Feature-Based** — Stories are organised around individual system capabilities (encryption, decryption, key management, feedback)
C) **Persona-Based** — Stories are grouped by user type (non-technical user, security-conscious user)
D) **Epic-Based** — Stories are structured as high-level epics (Encrypt Files, Decrypt Files) with child stories beneath each
E) Other (please describe after [Answer]: tag below)

[Answer]: A

---

### Question 2
How many user personas should be created for cryptLlama?

A) **One** — A single "General User" covering all skill levels
B) **Two** — A non-technical user (uses auto-key) and a security-conscious user (provides own password)
C) **Three** — Non-technical user, security-conscious user, and a developer/power user integrating cryptLlama into scripts
D) Other (please describe after [Answer]: tag below)

[Answer]: C

---

### Question 3
What level of detail should acceptance criteria be written at?

A) **High-level** — One or two criteria per story describing the observable outcome
B) **Standard** — Three to five criteria per story covering the happy path and one or two error cases
C) **Detailed** — Full Given/When/Then (BDD-style) scenarios for every story including all edge cases
D) Other (please describe after [Answer]: tag below)

[Answer]: B

---

### Question 4
Should the auto-generated key warning (FR-05) be treated as its own dedicated story, or folded into the general "encrypt without password" story?

A) **Dedicated story** — The warning behaviour is safety-critical and deserves its own acceptance criteria and testable specification
B) **Sub-task of encrypt story** — The warning is part of the encryption flow; handle it within the parent story's acceptance criteria
C) Other (please describe after [Answer]: tag below)

[Answer]: A

---

### Question 5
How should the interactive password prompt during decryption (FR-06) be covered?

A) **Separate story** — Treat hidden-input password prompting as its own story with specific UX acceptance criteria
B) **Part of the decrypt story** — Include it as acceptance criteria within the decryption story
C) Other (please describe after [Answer]: tag below)

[Answer]: B

---

### Question 6
Should error and feedback scenarios (FR-08) — such as wrong password, corrupt file, or missing file — be covered as separate stories or as acceptance criteria within existing stories?

A) **Separate error-handling stories** — Each major error scenario gets its own story (wrong password, file not found, corrupt/tampered file)
B) **Acceptance criteria only** — Error scenarios are written as criteria within the primary encrypt/decrypt stories
C) **Mix** — The most critical errors (corrupt file detected via GCM auth tag failure) get their own story; minor errors are acceptance criteria
D) Other (please describe after [Answer]: tag below)

[Answer]: B

---

### Question 7
Should extensibility (NFR-08 — future GUI, web, mobile support) be captured in any user stories, or is it strictly a non-functional constraint to be handled in design?

A) **Include an extensibility story** — A story such as "As a future developer, I want the core logic decoupled from the CLI so it can be wrapped by a GUI later"
B) **NFR only** — Keep extensibility as a non-functional requirement and architectural constraint; no user story needed
C) Other (please describe after [Answer]: tag below)

[Answer]: B

---

### Question 8
What story size/granularity is preferred — should stories be written to be completable in roughly one focused coding session, or is a larger epic-level granularity acceptable?

A) **Small (sprint-task size)** — Each story represents a single focussed piece of behaviour; fine-grained
B) **Medium (feature-size)** — Each story covers a complete feature end-to-end (e.g., all of "file encryption")
C) **Mixed** — Core flows (encrypt, decrypt) at medium size; supporting behaviours (error handling, key warning) at small size
D) Other (please describe after [Answer]: tag below)

[Answer]: C

---

## PART 2: Generation Steps

*These steps will be executed after the plan is approved.*

- [x] **Step G1**: Create `aidlc-docs/inception/user-stories/personas.md`
  - [x] Define each persona with name, technical level, goals, and frustrations
  - [x] Map each persona to the stories they are involved in

- [x] **Step G2**: Create `aidlc-docs/inception/user-stories/stories.md`
  - [x] Write stories in "As a [persona], I want [goal], so that [benefit]" format
  - [x] Apply INVEST criteria (Independent, Negotiable, Valuable, Estimable, Small, Testable)
  - [x] Include acceptance criteria per the agreed format (Q3)
  - [x] Organise stories using the agreed breakdown approach (Q1)
  - [x] Cover: file encryption, file decryption, auto-key generation, interactive password prompt, operation feedback, error handling

- [x] **Step G3**: Review all stories against the requirements document
  - [x] Verify every Functional Requirement (FR-01 through FR-08) is addressed by at least one story
  - [x] Verify Security constraints (NFR-01 through NFR-04) are reflected in acceptance criteria where user-visible
  - [x] Confirm NFR-05 (all skill levels) is represented in persona characteristics

- [x] **Step G4**: Update `aidlc-docs/aidlc-state.md`
  - [x] Mark User Stories stage complete

- [x] **Step G5**: Update audit.md with completion lo