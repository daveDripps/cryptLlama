package com.llamacrypt.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyManagerTest {
    private KeyManager keyManager;

    @BeforeEach
    void setUp() {
        keyManager = new KeyManager();
    }

    @Test
    void generateSalt_shouldReturn16Bytes() {
        byte[] salt = keyManager.generateSalt();
        assertEquals(16, salt.length);
    }

    @Test
    void generateIv_shouldReturn12Bytes() {
        byte[] iv = keyManager.generateIv();
        assertEquals(12, iv.length);
    }

    @Test
    void generateRandomKey_shouldReturn32Bytes() {
        byte[] key = keyManager.generateRandomKey();
        assertEquals(32, key.length);
    }

    @Test
    void generateSalt_shouldGenerateUniqueValues() {
        byte[] salt1 = keyManager.generateSalt();
        byte[] salt2 = keyManager.generateSalt();
        assertFalse(java.util.Arrays.equals(salt1, salt2));
    }

    @Test
    void generateRandomKey_shouldGenerateUniqueValues() {
        byte[] key1 = keyManager.generateRandomKey();
        byte[] key2 = keyManager.generateRandomKey();
        assertFalse(java.util.Arrays.equals(key1, key2));
    }

    @Test
    void deriveKey_shouldReturn32Bytes() {
        char[] password = "testPassword".toCharArray();
        byte[] salt = keyManager.generateSalt();
        Argon2Params params = Argon2Params.defaults();

        byte[] key = keyManager.deriveKey(password, salt, params);

        assertEquals(32, key.length);
    }

    @Test
    void deriveKey_sameInputsShouldProduceSameOutput() {
        char[] password = "testPassword".toCharArray();
        byte[] salt = keyManager.generateSalt();
        Argon2Params params = Argon2Params.defaults();

        byte[] key1 = keyManager.deriveKey(password, salt, params);
        byte[] key2 = keyManager.deriveKey(password, salt, params);

        assertArrayEquals(key1, key2);
    }

    @Test
    void deriveKey_differentSaltShouldProduceDifferentOutput() {
        char[] password = "testPassword".toCharArray();
        byte[] salt1 = keyManager.generateSalt();
        byte[] salt2 = keyManager.generateSalt();
        Argon2Params params = Argon2Params.defaults();

        byte[] key1 = keyManager.deriveKey(password, salt1, params);
        byte[] key2 = keyManager.deriveKey(password, salt2, params);

        assertFalse(java.util.Arrays.equals(key1, key2));
    }

    @Test
    void deriveKey_differentPasswordShouldProduceDifferentOutput() {
        byte[] salt = keyManager.generateSalt();
        Argon2Params params = Argon2Params.defaults();

        byte[] key1 = keyManager.deriveKey("password1".toCharArray(), salt, params);
        byte[] key2 = keyManager.deriveKey("password2".toCharArray(), salt, params);

        assertFalse(java.util.Arrays.equals(key1, key2));
    }

    @Test
    void clearSensitiveBytes_shouldZeroArray() {
        byte[] data = keyManager.generateRandomKey();
        byte[] copy = data.clone();

        keyManager.clearSensitiveBytes(data);

        assertArrayEquals(new byte[32], data);
    }

    @Test
    void clearSensitiveChars_shouldZeroArray() {
        char[] data = "sensitivePassword".toCharArray();
        char[] copy = data.clone();

        keyManager.clearSensitiveChars(data);

        assertArrayEquals(new char[data.length], data);
    }

    @Test
    void clearSensitiveBytes_shouldHandleNull() {
        assertDoesNotThrow(() -> keyManager.clearSensitiveBytes(null));
    }

    @Test
    void clearSensitiveChars_shouldHandleNull() {
        assertDoesNotThrow(() -> keyManager.clearSensitiveChars(null));
    }
}
