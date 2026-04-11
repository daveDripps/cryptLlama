package com.llamacrypt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionDecryptionRoundTripTest {
    private EncryptionService encryptionService;
    private DecryptionService decryptionService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        decryptionService = new DecryptionService();
    }

    @Test
    void roundTrip_withPassword_shouldRestoreOriginalContent() throws IOException {
        Path source = tempDir.resolve("roundtrip1.txt");
        byte[] originalContent = "This is a test file for encryption roundtrip.".getBytes();
        Files.write(source, originalContent);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "securePassword123".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());
        Files.delete(source);

        DecryptionResult decryptResult = decryptionService.decrypt(encryptedFile, "securePassword123".toCharArray());
        Path decryptedFile = Path.of(decryptResult.getOutputPath());

        assertArrayEquals(originalContent, Files.readAllBytes(decryptedFile));
    }

    @Test
    void roundTrip_withPassword_largeFile_shouldWork() throws IOException {
        Path source = tempDir.resolve("large.txt");
        byte[] originalContent = new byte[1024 * 100];
        new java.security.SecureRandom().nextBytes(originalContent);
        Files.write(source, originalContent);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "largeFilePassword".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());
        Files.delete(source);

        DecryptionResult decryptResult = decryptionService.decrypt(encryptedFile, "largeFilePassword".toCharArray());
        Path decryptedFile = Path.of(decryptResult.getOutputPath());

        assertArrayEquals(originalContent, Files.readAllBytes(decryptedFile));
    }

    @Test
    void roundTrip_withPassword_binaryData_shouldWork() throws IOException {
        Path source = tempDir.resolve("binary.bin");
        byte[] originalContent = new byte[256];
        for (int i = 0; i < 256; i++) {
            originalContent[i] = (byte) i;
        }
        Files.write(source, originalContent);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "binaryPassword".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());
        Files.delete(source);

        DecryptionResult decryptResult = decryptionService.decrypt(encryptedFile, "binaryPassword".toCharArray());
        Path decryptedFile = Path.of(decryptResult.getOutputPath());

        assertArrayEquals(originalContent, Files.readAllBytes(decryptedFile));
    }

    @Test
    void roundTrip_withPassword_emptyFile_shouldWork() throws IOException {
        Path source = tempDir.resolve("empty.txt");
        byte[] originalContent = new byte[0];
        Files.write(source, originalContent);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "emptyPassword".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());
        Files.delete(source);

        DecryptionResult decryptResult = decryptionService.decrypt(encryptedFile, "emptyPassword".toCharArray());
        Path decryptedFile = Path.of(decryptResult.getOutputPath());

        assertArrayEquals(originalContent, Files.readAllBytes(decryptedFile));
    }
}
