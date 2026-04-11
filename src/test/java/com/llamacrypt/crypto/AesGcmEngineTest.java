package com.llamacrypt.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AesGcmEngineTest {
    private AesGcmEngine engine;
    private KeyManager keyManager;
    private byte[] key;
    private byte[] iv;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        engine = new AesGcmEngine();
        keyManager = new KeyManager();
        key = keyManager.generateRandomKey();
        iv = keyManager.generateIv();
    }

    @Test
    void encryptDecrypt_shouldRestoreOriginalData() {
        byte[] plaintext = "Hello, World!".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(plaintext);
        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();

        engine.encryptStream(in, encryptedOut, key, iv);

        byte[] ciphertext = encryptedOut.toByteArray();
        assertNotEquals(plaintext, ciphertext);

        ByteArrayInputStream decryptIn = new ByteArrayInputStream(ciphertext);
        ByteArrayOutputStream decryptedOut = new ByteArrayOutputStream();

        engine.decryptStream(decryptIn, decryptedOut, key, iv);

        assertArrayEquals(plaintext, decryptedOut.toByteArray());
    }

    @Test
    void encrypt_shouldProduceDifferentOutputWithDifferentIV() {
        byte[] plaintext = "Test data".getBytes();
        byte[] iv2 = keyManager.generateIv();

        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();

        engine.encryptStream(new ByteArrayInputStream(plaintext), out1, key, iv);
        engine.encryptStream(new ByteArrayInputStream(plaintext), out2, key, iv2);

        assertFalse(java.util.Arrays.equals(out1.toByteArray(), out2.toByteArray()));
    }

    @Test
    void decrypt_withWrongKey_shouldThrowDecryptionException() {
        byte[] plaintext = "Secret data".getBytes();
        byte[] wrongKey = keyManager.generateRandomKey();

        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
        engine.encryptStream(new ByteArrayInputStream(plaintext), encryptedOut, key, iv);

        assertThrows(DecryptionException.class, () ->
                engine.decryptStream(
                        new ByteArrayInputStream(encryptedOut.toByteArray()),
                        new ByteArrayOutputStream(),
                        wrongKey,
                        iv
                )
        );
    }

    @Test
    void decrypt_withTamperedCiphertext_shouldThrowDecryptionException() {
        byte[] plaintext = "Important file content".getBytes();

        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
        engine.encryptStream(new ByteArrayInputStream(plaintext), encryptedOut, key, iv);

        byte[] ciphertext = encryptedOut.toByteArray();
        ciphertext[0] ^= 0xFF;

        assertThrows(DecryptionException.class, () ->
                engine.decryptStream(
                        new ByteArrayInputStream(ciphertext),
                        new ByteArrayOutputStream(),
                        key,
                        iv
                )
        );
    }

    @Test
    void encryptDecrypt_largeData_shouldWork() {
        byte[] plaintext = new byte[1024 * 100];
        new java.security.SecureRandom().nextBytes(plaintext);

        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
        engine.encryptStream(new ByteArrayInputStream(plaintext), encryptedOut, key, iv);

        ByteArrayOutputStream decryptedOut = new ByteArrayOutputStream();
        engine.decryptStream(
                new ByteArrayInputStream(encryptedOut.toByteArray()),
                decryptedOut,
                key,
                iv
        );

        assertArrayEquals(plaintext, decryptedOut.toByteArray());
    }

    @Test
    void encryptDecrypt_emptyData_shouldWork() {
        byte[] plaintext = new byte[0];

        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
        engine.encryptStream(new ByteArrayInputStream(plaintext), encryptedOut, key, iv);

        ByteArrayOutputStream decryptedOut = new ByteArrayOutputStream();
        engine.decryptStream(
                new ByteArrayInputStream(encryptedOut.toByteArray()),
                decryptedOut,
                key,
                iv
        );

        assertArrayEquals(plaintext, decryptedOut.toByteArray());
    }

    @Test
    void encryptDecrypt_binaryData_shouldWork() {
        byte[] plaintext = new byte[256];
        for (int i = 0; i < 256; i++) {
            plaintext[i] = (byte) i;
        }

        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
        engine.encryptStream(new ByteArrayInputStream(plaintext), encryptedOut, key, iv);

        ByteArrayOutputStream decryptedOut = new ByteArrayOutputStream();
        engine.decryptStream(
                new ByteArrayInputStream(encryptedOut.toByteArray()),
                decryptedOut,
                key,
                iv
        );

        assertArrayEquals(plaintext, decryptedOut.toByteArray());
    }
}
