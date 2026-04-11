# LlamaCrypt Roadmap

## Vision
A secure, user-friendly file encryption tool for everyone — from CLI power users to casual desktop users.

---

## Phase 1 ✅ Complete (2026-04-11)
- CLI encryption tool with AES-256-GCM + Argon2id
- Password-based and key-based encryption
- GitHub Actions CI/CD

---

## Phase 2 🔄 Planning

### Features
- [ ] **Windows Desktop Interface**: Native Windows desktop app with drag-and-drop encryption
- [ ] **GUI Application**: Desktop or web interface for non-technical users
- [ ] **Batch Processing**: Encrypt/decrypt multiple files at once
- [ ] **File Shredder**: Securely delete original files after encryption
- [ ] **Password Manager Integration**: Store/retrieve passwords securely
- [ ] **Key Sharing**: Securely share encryption keys with others
- [ ] **Cloud Integration**: Encrypt directly to/from cloud storage

### Testing
- [ ] **Cucumber/Scenario Testing**: BDD-style acceptance tests using Gherkin scenarios
- [ ] **GUI Testing**: Automated UI testing if GUI is implemented

### Improvements
- [ ] **Performance**: GPU acceleration for key derivation
- [ ] **Compression**: Optional compression before encryption
- [ ] **Key Backup**: Secure key backup/recovery mechanism
- [ ] **Brute Force Protection**: Rate limiting
- [ ] **YubiKey/Hardware Token Support**: Hardware keys for encryption
- [ ] **Cross-Platform CLI Improvements**: Better shell integration

### Documentation
- [ ] **Security Whitepaper**: Formal security analysis
- [ ] **API Documentation**: For library usage
- [ ] **Video Tutorials**: How-to guides for non-technical users

---

## Future Ideas (Backlog)
- Mobile App (iOS/Android)
- Plugin System
- Audit Logging
- Browser Extension

---

*Last Updated: 2026-04-11*
