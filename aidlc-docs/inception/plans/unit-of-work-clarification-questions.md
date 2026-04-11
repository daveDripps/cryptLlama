# Unit of Work — Clarification Questions (Unit 0 Addition)

Please answer each question by filling in the letter after the `[Answer]:` tag.
Let me know when you are done.

---

## Question 1
Which Git hosting platform should the repository be created on?

A) GitHub (github.com)
B) Other (please describe after [Answer]: tag below)

[Answer]: A do you need fine grain access token or what do you need here?

---

## Question 2
What should the repository name be?

A) `cryptLlama` (matches the project name exactly)
B) `crypt-llama` (kebab-case, common GitHub convention)
C) Other (please describe after [Answer]: tag below)

[Answer]: Llama Crypt (in fact we should scrap crypt llama altogether and just use LlamaCrypt instead)

---

## Question 3
Should the repository be public or private?

A) Private
B) Public
C) Other (please describe after [Answer]: tag below)

[Answer]: public

---

## Question 4
What branch strategy should be used for development across units?

A) **Feature branch per unit** — each unit gets its own branch
   (e.g. `feature/unit-01-crypto-io`, `feature/unit-02-services`, `feature/unit-03-cli`).
   At the end of each unit, a PR is opened from the unit branch → `main`.
   *Best for: clean PR history, one PR per unit, no shared dev branch.*

B) **Single `develop` branch** — all unit work is committed to `develop`.
   At the end of each unit, a PR is opened from `develop` → `main`.
   *Best for: simpler branching, continuous integration feel.*

C) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Question 5
For the initial stub (Unit 0 deliverable), what should be included in the repository?

A) **Minimum viable build** — empty `Main.java`, `build.gradle` (Java 21, Bouncy Castle
   dependency declared, shadow JAR plugin), `settings.gradle`, `.gitignore`, and a
   GitHub Actions workflow that compiles and runs unit tests on every push/PR.
   *This gives a green build from day one.*

B) **Bare minimum** — empty `Main.java` and a basic `build.gradle` only (no workflow yet;
   workflow added as a separate step).

C) Other (please describe after [Answer]: tag below)

[Answer]: A

---

## Question 6
Should the GitHub Actions CI build be a **required status check** on the main branch
protection rule — i.e., a PR cannot be merged to `main` unless the build passes?

A) Yes — CI must pass before any PR can be merged to `main`
B) No — branch protection only requires PR review; CI is informational
C) Other (please describe after [Answer]: tag below)

[Answer]: A
