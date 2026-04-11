package com.llamacrypt.service;

import com.llamacrypt.crypto.*;
import com.llamacrypt.format.*;
import com.llamacrypt.io.*;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public final class EncryptionService {
    private final KeyManager keyManager;
    private final AesGcmEngine engine;
    private final FileFormatHandler formatHandler;
    private final StreamProcessor processor;

    public EncryptionService() {
        this.keyManager = new KeyManager();
        this.engine = new AesGcmEngine();
        this.formatHandler = new FileFormatHandler();
        this.processor = new StreamProcessor();
    }

    public EncryptionResult encrypt(Path sourcePath, char[] password) {
        byte[] salt = keyManager.generateSalt();
        byte[] iv = keyManager.generateIv();
        Argon2Params params = Argon2Params.defaults();

        byte[] derivedKey = keyManager.deriveKey(password, salt, params);
        keyManager.clearSensitiveChars(password);

        Path outputPath = Paths.get(sourcePath + ".lcrypt");

        try {
            ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
            LcryptHeader header = new LcryptHeader(params, salt, iv);
            formatHandler.writeHeader(headerStream, header);

            processor.process(sourcePath, outputPath, (in, out) -> {
                out.write(headerStream.toByteArray());
                engine.encryptStream(in, out, derivedKey, iv);
            });

            keyManager.clearSensitiveBytes(derivedKey);
            return EncryptionResult.withoutKey(outputPath.toString());
        } catch (Exception e) {
            keyManager.clearSensitiveBytes(derivedKey);
            throw new ServiceException("Encryption failed: " + e.getMessage(), e);
        }
    }

    public EncryptionResult encryptWithoutPassword(Path sourcePath) {
        byte[] salt = keyManager.generateSalt();
        byte[] iv = keyManager.generateIv();
        Argon2Params params = Argon2Params.defaults();

        byte[] key = keyManager.generateRandomKey();

        Path outputPath = Paths.get(sourcePath + ".lcrypt");

        try {
            ByteArrayOutputStream headerStream = new ByteArrayOutputStream();
            LcryptHeader header = new LcryptHeader(params, salt, iv);
            formatHandler.writeHeader(headerStream, header);

            processor.process(sourcePath, outputPath, (in, out) -> {
                out.write(headerStream.toByteArray());
                engine.encryptStream(in, out, key, iv);
            });

            String keyBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(key);
            keyManager.clearSensitiveBytes(key);

            return EncryptionResult.withKey(outputPath.toString(), keyBase64);
        } catch (Exception e) {
            keyManager.clearSensitiveBytes(key);
            throw new ServiceException("Encryption failed: " + e.getMessage(), e);
        }
    }
}
