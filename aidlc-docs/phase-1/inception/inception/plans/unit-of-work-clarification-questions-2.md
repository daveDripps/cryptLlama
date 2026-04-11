# Unit of Work — Follow-up Clarification Questions

Three small things to resolve before updating all the artefacts.

---

## Question 1
Q6 from the previous file appeared truncated. Confirming: should the GitHub Actions
CI build be a **required status check** before a PR can be merged to `main`?

A) Yes — CI must pass before any PR can merge to `main`
B) No — branch protection requires a PR but CI passing is optional
C) Other (please describe after [Answer]: tag below)

[Answer]: 

---

## Question 2
With the rename to **LlamaCrypt**, what should the CLI command name be?
(This is what users will type in the terminal.)

A) `llamacrypt` — all lowercase, clean binary name (e.g. `llamacrypt myfile.txt mypassword`)
B) `llamaCrypt` — camelCase matching the project name capitalisation
C) `llama-crypt` — kebab-case
D) Other (please describe after [Answer]: tag below)

[Answer]: 

---

## Question 3
With the rename to **LlamaCrypt**, what should the encrypted file extension be?
(Previously `.cllama`, derived from "cryptLlama".)

A) `.llama` — short, clean, derived from the llama brand
B) `.lcrypt` — llama-crypt abbreviation, more descriptive
C) `.llamacrypt` — full name (longer but unambiguous)
D) Keep `.cllama` — no strong reason to change it
E) Other (please describe after [Answer]: tag below)

[Answer]: 

---

**On GitHub authentication (your Q1 note):**
No tokens or credentials needed from you right now — everything stays in the docs
at this stage. When we reach Unit 0 execution (Construction phase), I will use the
`gh` GitHub CLI tool which should already be authenticated on your machine. If it
isn't, I will walk you through a one-time `gh auth login` at that point. Nothing
sensitive is needed in the planning documents.
