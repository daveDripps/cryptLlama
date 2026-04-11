# LlamaCrypt

A secure file encryption tool using AES-256-GCM with Argon2id key derivation.

## Features

- **Password-based encryption**: Encrypt files with a password you choose
- **Key-based encryption**: Encrypt files with an auto-generated key (safer for sensitive data)
- **Secure by default**: AES-256-GCM encryption with Argon2id key derivation (64 MiB memory)
- **Streaming**: Handles large files efficiently without loading entire file into memory
- **Plain-language errors**: Clear error messages, no technical jargon

## Installation

### Prerequisites
- Java 17 or later
- Gradle 8.x (or use the included wrapper)

### Build from source
```bash
./gradlew shadowJar
```

This creates `build/libs/lcrypt.jar`

### Or download release
Download the latest `lcrypt.jar` from the releases page.

## Usage

```
lcrypt <command> <filename> [password]
```

### Commands

| Command | Description |
|---------|-------------|
| `encrypt` | Encrypt a file with a password |
| `encrypt-key` | Encrypt a file with an auto-generated key |
| `decrypt` | Decrypt an encrypted file |

### Examples

**Encrypt with password:**
```bash
java -jar lcrypt.jar encrypt myfile.txt mypassword
```

**Encrypt with auto-generated key:**
```bash
java -jar lcrypt.jar encrypt-key secret.docx
```
This generates a key that you must save securely. If you lose it, your data cannot be recovered.

**Decrypt a file:**
```bash
java -jar lcrypt.jar decrypt myfile.txt.lcrypt mypassword
```

### Interactive mode

If you omit the password, LlamaCrypt will prompt you:
```bash
java -jar lcrypt.jar encrypt myfile.txt
Enter password: ********
```

## Security Notes

- Files are encrypted with AES-256-GCM (authenticated encryption)
- Passwords are derived using Argon2id (memory-hard, GPU-resistant)
- Sensitive data is cleared from memory after use
- Original files are never modified or deleted
- Encrypted files have `.lcrypt` extension

## File Format

Encrypted files (`.lcrypt`) contain:
- Magic bytes: `LCRY`
- Argon2id parameters (memory: 64 MiB, iterations: 3)
- Salt and IV (unique per encryption)
- Encrypted content with authentication tag

## Troubleshooting

**"File not found"**
- Check the file path is correct
- Use absolute paths if relative paths don't work

**"Incorrect password or the file is corrupted"**
- Verify you used the correct password
- Check the file wasn't modified after encryption

**"Not a valid encrypted file"**
- Make sure you're decrypting a `.lcrypt` file
- File may be corrupted or not a LlamaCrypt file

## License

MIT License
