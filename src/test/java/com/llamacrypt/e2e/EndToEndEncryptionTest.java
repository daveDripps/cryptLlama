package com.llamacrypt.e2e;

import com.llamacrypt.service.DecryptionResult;
import com.llamacrypt.service.EncryptionResult;
import com.llamacrypt.service.EncryptionService;
import com.llamacrypt.service.DecryptionService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EndToEndEncryptionTest {
    private static Path tempDir;
    private static Path sourceFile;
    private static Path encryptedFile;
    private static Path decryptedFile;
    private static final String TEST_PASSWORD = "e2e-test-password-123";

    private static final String LOREM_IPSUM = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
            Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
            Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris.
            Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore.
            Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt.
            """;

    @BeforeAll
    static void setup() throws IOException {
        tempDir = Files.createTempDirectory("lcrypt-e2e");
        sourceFile = tempDir.resolve("test-file.txt");
        Files.writeString(sourceFile, LOREM_IPSUM);
    }

    @AfterAll
    static void cleanup() {
        deleteIfExists(sourceFile);
        deleteIfExists(encryptedFile);
        deleteIfExists(decryptedFile);
        deleteIfExists(tempDir);
    }

    private static void deleteIfExists(Path path) {
        try {
            if (path != null && Files.exists(path)) {
                Files.deleteIfExists(path);
            }
        } catch (IOException ignored) {
            // Ignore cleanup errors
        }
    }

    @Test
    void endToEndEncryptionDecryption() throws IOException {
        EncryptionService encryptionService = new EncryptionService();
        DecryptionService decryptionService = new DecryptionService();

        EncryptionResult encryptResult = encryptionService.encrypt(sourceFile, TEST_PASSWORD.toCharArray());
        assertNotNull(encryptResult.getOutputPath(), "Encryption should return output path");
        assertTrue(encryptResult.getOutputPath().endsWith(".lcrypt"), "Encrypted file should have .lcrypt extension");
        
        encryptedFile = Path.of(encryptResult.getOutputPath());
        assertTrue(Files.exists(encryptedFile), "Encrypted file should exist");

        String decryptedPath = encryptResult.getOutputPath().replace(".lcrypt", "");
        decryptedFile = Path.of(decryptedPath);

        DecryptionResult decryptResult = decryptionService.decrypt(encryptedFile, TEST_PASSWORD.toCharArray());
        assertNotNull(decryptResult.getOutputPath(), "Decryption should return output path");

        String originalContent = Files.readString(sourceFile);
        String decryptedContent = Files.readString(decryptedFile);

        assertEquals(originalContent, decryptedContent, "Decrypted content should match original");
        
        System.out.println("E2E Test PASSED: File encrypted and decrypted successfully");
    }
}