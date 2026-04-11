package com.llamacrypt.crypto;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.security.SecureRandom;
import java.util.Arrays;

public final class KeyManager {
    private static final int KEY_LENGTH_BYTES = 32;
    private static final int SALT_LENGTH_BYTES = 16;
    private static final int IV_LENGTH_BYTES = 12;
    private static final int ARGON2_ID = 2;

    private final SecureRandom secureRandom;

    public KeyManager() {
        this.secureRandom = new SecureRandom();
    }

    public byte[] deriveKey(char[] password, byte[] salt, Argon2Params params) {
        try {
            Argon2Parameters argon2Params = new Argon2Parameters.Builder(ARGON2_ID)
                    .withMemoryAsKB(params.getMemoryCostKiB())
                    .withIterations(params.getIterations())
                    .withParallelism(params.getParallelism())
                    .withSalt(salt)
                    .build();

            Argon2BytesGenerator generator = new Argon2BytesGenerator();
            generator.init(argon2Params);

            byte[] derivedKey = new byte[KEY_LENGTH_BYTES];
            generator.generateBytes(password, derivedKey);
            return derivedKey;
        } catch (Exception e) {
            throw new KeyDerivationException("Key derivation failed", e);
        }
    }

    public byte[] generateRandomKey() {
        byte[] key = new byte[KEY_LENGTH_BYTES];
        secureRandom.nextBytes(key);
        return key;
    }

    public byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        secureRandom.nextBytes(salt);
        return salt;
    }

    public byte[] generateIv() {
        byte[] iv = new byte[IV_LENGTH_BYTES];
        secureRandom.nextBytes(iv);
        return iv;
    }

    public void clearSensitiveBytes(byte[] data) {
        if (data != null) {
            clearByteArray(data);
        }
    }

    public void clearSensitiveChars(char[] data) {
        if (data != null) {
            Arrays.fill(data, (char) 0);
        }
    }

    private void clearByteArray(byte[] array) {
        Arrays.fill(array, (byte) 0);
    }
}
