# cryptLlama — User Personas

Three personas are defined for cryptLlama Phase 1 (CLI). They represent the full spectrum of
users described in NFR-05 (accessible to all technical skill levels).

---

## Persona 1 — Maya (Non-Technical User)

| Attribute | Detail |
|-----------|--------|
| **Name** | Maya |
| **Role** | Office administrator / general computer user |
| **Technical Level** | Low — comfortable with everyday software but has no knowledge of encryption, cryptography, or the command line beyond basic use |
| **Operating System** | Windows (primary), occasionally macOS |

### Goals
- Protect sensitive documents (contracts, personal records, HR files) before sharing or storing them
- Get a task done quickly without needing to understand how it works under the hood
- Feel confident the file is protected even if she is not sure what "AES-256" means

### Frustrations
- Technical jargon in error messages that she cannot act on
- Tools that silently fail or produce no feedback
- Being expected to remember or manage cryptographic keys without guidance
- Multi-step processes when a single command would do

### Behaviour Patterns
- Will run cryptLlama without a password, relying on auto-key generation
- Relies entirely on the console output to know whether the operation worked
- May not notice a warning unless it is visually prominent and in plain language

### Mapped Stories
- US-02 (encrypt without password)
- US-03 (auto-key generation warning)
- US-04 (decrypt a file)
- US-05 (operation feedback)

---

## Persona 2 — Alex (Security-Conscious User)

| Attribute | Detail |
|-----------|--------|
| **Name** | Alex |
| **Role** | IT professional / privacy-aware individual |
| **Technical Level** | Medium-High — understands encryption concepts, comfortable with the command line, chooses tools deliberately |
| **Operating System** | macOS / Linux |

### Goals
- Control over the password/key used for encryption — does not trust auto-generated keys stored only in console history
- Confidence that a strong, well-known algorithm (AES-256-GCM) is being used
- Clean, predictable CLI behaviour with no surprises
- Ability to securely share encrypted files with others using a shared passphrase

### Frustrations
- Tools that obscure what algorithm or settings they use
- Auto-key generation that produces unclear output
- Password input being echoed to the terminal
- Lack of authentication/integrity checking (important: GCM provides this)

### Behaviour Patterns
- Always supplies a password on the command line or via interactive prompt
- Inspects console output carefully to verify the correct file was processed
- May encrypt the same file multiple times with different passwords for testing

### Mapped Stories
- US-01 (encrypt with password)
- US-04 (decrypt a file)
- US-05 (operation feedback)

---

## Persona 3 — Jordan (Developer / Power User)

| Attribute | Detail |
|-----------|--------|
| **Name** | Jordan |
| **Role** | Software developer / DevOps engineer |
| **Technical Level** | High — expert command-line user, writes scripts and automation pipelines |
| **Operating System** | Linux / macOS (primarily), Windows via WSL |

### Goals
- Integrate cryptLlama into shell scripts and CI/CD pipelines without interactive prompts breaking automation
- Predictable, non-zero exit codes on failure so scripts can branch on success/failure
- Consistent, parseable console output (or at minimum, quiet on success)
- Confidence the tool handles large files efficiently (streaming I/O — NFR-07)

### Frustrations
- Interactive prompts that block non-interactive script execution
- Ambiguous exit codes (everything exits 0 regardless of outcome)
- Verbose output that pollutes pipeline logs
- Inconsistent behaviour across platforms

### Behaviour Patterns
- Always supplies the password as a command-line argument (never uses interactive prompt in automation)
- Checks exit codes programmatically
- May encrypt/decrypt many files in a loop

### Mapped Stories
- US-01 (encrypt with password — primary journey for scripting)
- US-04 (decrypt a file — always with password on command line)
- US-05 (operation feedback — especially exit code behaviour)
