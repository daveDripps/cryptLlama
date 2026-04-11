package com.llamacrypt.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class DecryptionServiceTest {
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
    void decrypt_withCorrectPassword_shouldRestoreOriginalFile() throws IOException {
        Path source = tempDir.resolve("original.txt");
        byte[] content = "Secret data".getBytes();
        Files.write(source, content);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "password123".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());

        DecryptionResult decryptResult = decryptionService.decrypt(encryptedFile, "password123".toCharArray());
        Path decryptedFile = Path.of(decryptResult.getOutputPath());

        assertArrayEquals(content, Files.readAllBytes(decryptedFile));
    }

    @Test
    void decrypt_withWrongPassword_shouldThrowServiceException() throws IOException {
        Path source = tempDir.resolve("original.txt");
        byte[] content = "Secret data".getBytes();
        Files.write(source, content);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "correctPassword".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());

        ServiceException exception = assertThrows(ServiceException.class, () ->
                decryptionService.decrypt(encryptedFile, "wrongPassword".toCharArray())
        );

        assertEquals("Incorrect password or the file is corrupted", exception.getMessage());
    }

    @Test
    void decrypt_withNonExistentFile_shouldThrowServiceException() {
        Path nonExistent = tempDir.resolve("nonexistent.lcrypt");

        assertThrows(ServiceException.class, () ->
                decryptionService.decrypt(nonExistent, "password".toCharArray())
        );
    }

    @Test
    void decrypt_withInvalidFile_shouldThrowServiceException() throws IOException {
        Path invalidFile = tempDir.resolve("invalid.lcrypt");
        Files.write(invalidFile, "Not a valid encrypted file".getBytes());

        assertThrows(ServiceException.class, () ->
                decryptionService.decrypt(invalidFile, "password".toCharArray())
        );
    }

    @Test
    void decrypt_withTamperedFile_shouldThrowServiceException() throws IOException {
        Path source = tempDir.resolve("original.txt");
        byte[] content = "Secret data".getBytes();
        Files.write(source, content);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "password".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());

        byte[] encryptedContent = Files.readAllBytes(encryptedFile);
        encryptedContent[50] ^= 0xFF;
        Files.write(encryptedFile, encryptedContent);

        assertThrows(ServiceException.class, () ->
                decryptionService.decrypt(encryptedFile, "password".toCharArray())
        );
    }

    @Test
    void decrypt_shouldClearPasswordFromMemory() throws IOException {
        Path source = tempDir.resolve("original.txt");
        byte[] content = "Test".getBytes();
        Files.write(source, content);

        EncryptionResult encryptResult = encryptionService.encrypt(source, "password".toCharArray());
        Path encryptedFile = Path.of(encryptResult.getOutputPath());

        char[] password = "password".toCharArray();
        decryptionService.decrypt(encryptedFile, password);

        assertEquals(0, password[0]);
    }
}
