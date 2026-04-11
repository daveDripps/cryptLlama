# LlamaCrypt — User Stories

**Breakdown approach**: User Journey-Based  
**Acceptance criteria depth**: Standard (3–5 criteria per story; happy path + key error cases)  
**Granularity**: Mixed — core flows (US-01, US-02, US-04, US-05) at medium size; US-03 at small size  
**INVEST compliance**: Each story is Independent, Negotiable, Valuable, Estimable, Small, Testable

---

## Journey 1 — Protecting a File (Encryption)

---

### US-01 — Encrypt a File with a Password (Medium)

**Story**  
As **Alex (security-conscious user)** or **Jordan (developer/power user)**, I want to encrypt a file by supplying my own password on the command line, so that only someone who knows that password can decrypt the file.

**Command form**
```
lcrypt <sourcefile> <password>
```

**Acceptance Criteria**

1. **Happy path — output file created**: Given a valid, readable source file and a non-empty password argument, running `lcrypt <sourcefile> <password>` produces an encrypted output file named `<sourcefile>.lcrypt` in the same directory as the source file.
2. **Source file preserved**: The original source file is not modified or deleted after a successful encryption.
3. **Success feedback**: The console prints a plain-language success message that includes the full path of the newly created `.lcrypt` file.
4. **Missing file — error feedback**: Given a source file path that does not exist, the application prints a plain-language error message and exits without creating any output file.
5. **Unreadable file — error feedback**: Given a source file that exists but cannot be read (e.g., permission denied), the application prints a plain-language error message and exits without creating any output file.

**Requirements traceability**: FR-01, FR-03, FR-04, FR-07, FR-08, NFR-01, NFR-02, NFR-03

---

### US-02 — Encrypt a File Without a Password (Medium)

**Story**  
As **Maya (non-technical user)**, I want to encrypt a file without supplying a password, so that I can protect my file without having to create or remember a cryptographic key myself.

**Command form**
```
lcrypt <sourcefile>
```

**Acceptance Criteria**

1. **Happy path — output file created**: Given a valid, readable source file and no password argument, running `lcrypt <sourcefile>` produces an encrypted output file named `<sourcefile>.lcrypt` in the same directory as the source file.
2. **Auto-key displayed**: The cryptographically strong key that was auto-generated for this file is printed to the console in full, in a format easy to copy (e.g., a hex or Base64 string on its own line).
3. **Source file preserved**: The original source file is not modified or deleted after a successful encryption.
4. **Success feedback**: The console prints a plain-language success message including the path of the `.lcrypt` output file.
5. **Missing file — error feedback**: Given a source file path that does not exist, the application prints a plain-language error message and exits without creating any output file.

**Requirements traceability**: FR-01, FR-03, FR-05, FR-07, FR-08, NFR-01, NFR-03, NFR-05

---

### US-03 — Auto-Key Generation Warning (Small — Dedicated)

**Story**  
As **Maya (non-technical user)**, I want to receive an unmissable warning when a key is auto-generated for my file, so that I understand I must save that key or I will permanently lose access to my file.

**Note**: This story captures the warning behaviour only. The encryption itself is covered in US-02. It is a dedicated story because the warning is safety-critical UX — failing to notice it results in irreversible data loss.

**Acceptance Criteria**

1. **Warning is prominent**: When a file is encrypted without a password, a warning message is displayed that is visually distinct from normal output — for example, prefixed with `WARNING:` or `⚠ WARNING:` — so it cannot be overlooked.
2. **Warning content is clear**: The warning message states in plain language that the file cannot be decrypted without the displayed key, and that the key will not be stored anywhere by the application.
3. **Warning appears before success message**: The warning and key are output to the console before the success confirmation line, ensuring the user sees the key before the operation is considered "done".
4. **Key is on its own line**: The auto-generated key is printed on a dedicated, clearly labelled line (e.g., `  Key: <key value>`) so it is easy to identify and copy without confusion.

**Requirements traceability**: FR-05, NFR-05

---

## Journey 2 — Accessing a Protected File (Decryption)

---

### US-04 — Decrypt a `.lcrypt` File (Medium)

**Story**  
As **Maya**, **Alex**, or **Jordan**, I want to decrypt a `.lcrypt` file back to its original format, so that I can access the original content after it has been encrypted.

**Command forms**
```
lcrypt <file.lcrypt> <password>     # password on command line
lcrypt <file.lcrypt>                # interactive password prompt
```

**Acceptance Criteria**

1. **Happy path — file restored**: Given a valid `.lcrypt` file and the correct password (supplied either on the command line or via interactive prompt), the application produces the decrypted file with its original filename (`.lcrypt` extension removed) in the same directory.
2. **Interactive prompt hides input**: When no password is provided on the command line during decryption, the application prompts the user for the password interactively; characters typed are not echoed to the terminal.
3. **Success feedback**: The console prints a plain-language success message including the path of the restored output file.
4. **Wrong password — error feedback**: Given a valid `.lcrypt` file but an incorrect password, the application prints a plain-language error message (e.g., "Incorrect password or the file is corrupted") and exits without creating any output file.
5. **Corrupt or tampered file — error feedback**: Given a `.lcrypt` file whose contents have been altered since encryption (detected via GCM authentication tag failure), the application prints a plain-language error message indicating the file cannot be decrypted and exits without creating any output file.

**Requirements traceability**: FR-02, FR-03, FR-04, FR-06, FR-07, FR-08, NFR-01, NFR-04

---

## Journey 3 — Understanding the Outcome (Feedback)

---

### US-05 — Receive Clear Feedback on Operation Outcome (Medium)

**Story**  
As **any user** (Maya, Alex, or Jordan), I want to receive clear, plain-language feedback after every operation, so that I always know whether my file was successfully encrypted or decrypted and what to do if something went wrong.

**Acceptance Criteria**

1. **Plain-language success messages**: All success outcomes display a message a non-technical user (Maya) can understand without cryptography knowledge — e.g., "File encrypted successfully: output.txt.lcrypt" rather than technical log output.
2. **Plain-language error messages**: All failure outcomes display a message that explains what went wrong in plain language without exposing internal implementation details (no stack traces, class names, or cryptographic internals).
3. **Non-zero exit code on failure**: The application exits with a non-zero exit code for every failure scenario, enabling Jordan to detect failures reliably in scripts without parsing console output.
4. **Zero exit code on success**: The application exits with exit code 0 for every successful operation.

**Requirements traceability**: FR-08, NFR-05

---

## Requirements Traceability Summary

| Requirement | Covered By |
|-------------|------------|
| FR-01 File Encryption | US-01, US-02 |
| FR-02 File Decryption | US-04 |
| FR-03 CLI usage pattern | US-01, US-02, US-04 |
| FR-04 Password-based key derivation | US-01, US-04 |
| FR-05 Auto-generated key (no password) | US-02, US-03 |
| FR-06 Interactive password prompt | US-04 (AC-2) |
| FR-07 Encrypted file format/header | US-01 (AC-1), US-04 (AC-1, AC-5) |
| FR-08 Encryption/decryption feedback | US-01, US-02, US-04, US-05 |
| NFR-01 AES-256-GCM | US-01, US-04 (AC-5 verifies auth tag) |
| NFR-02 Key derivation strength | US-01, US-04 |
| NFR-03 Secure RNG | US-01, US-02 |
| NFR-04 Memory handling (keys/passwords) | US-04 (AC-2 hidden input) |
| NFR-05 All skill levels / plain language | US-02, US-03, US-05 (explicitly) + all stories |
