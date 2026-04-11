package com.llamacrypt.service;

import com.llamacrypt.crypto.*;
import com.llamacrypt.format.*;
import com.llamacrypt.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class DecryptionService {
    private final KeyManager keyManager;
    private final AesGcmEngine engine;
    private final FileFormatHandler formatHandler;

    public DecryptionService() {
        this.keyManager = new KeyManager();
        this.engine = new AesGcmEngine();
        this.formatHandler = new FileFormatHandler();
    }

    public DecryptionResult decrypt(Path sourcePath, char[] password) {
        if (!sourcePath.toString().endsWith(".lcrypt")) {
            throw new ServiceException("Not a valid encrypted file");
        }

        try (InputStream fileIn = Files.newInputStream(sourcePath)) {
            LcryptHeader header = formatHandler.readHeader(fileIn);

            byte[] derivedKey = keyManager.deriveKey(password, header.getSalt(), header.getArgon2Params());
            keyManager.clearSensitiveChars(password);

            String outputPath = sourcePath.toString().replace(".lcrypt", "");
            Path destPath = Paths.get(outputPath);

            try (InputStream decryptIn = new SkippingInputStream(
                    Files.newInputStream(sourcePath), 42);
                 OutputStream fileOut = Files.newOutputStream(destPath,
                         StandardOpenOption.CREATE,
                         StandardOpenOption.TRUNCATE_EXISTING)) {
                engine.decryptStream(decryptIn, fileOut, derivedKey, header.getIv());
            } finally {
                keyManager.clearSensitiveBytes(derivedKey);
            }

            return DecryptionResult.success(outputPath);
        } catch (EncryptionException | DecryptionException e) {
            throw new ServiceException("Incorrect password or the file is corrupted", e);
        } catch (IOException e) {
            throw new ServiceException("Failed to read encrypted file", e);
        } catch (FormatException e) {
            throw new ServiceException("Not a valid encrypted file", e);
        }
    }

    private static class SkippingInputStream extends InputStream {
        private final InputStream delegate;
        private long remaining;

        SkippingInputStream(InputStream delegate, long toSkip) throws IOException {
            this.delegate = delegate;
            long skipped = delegate.skip(toSkip);
            this.remaining = toSkip - skipped;
        }

        @Override
        public int read() throws IOException {
            if (remaining > 0) {
                delegate.read(new byte[(int) remaining], 0, (int) remaining);
                remaining = 0;
            }
            return delegate.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (remaining > 0) {
                delegate.read(new byte[(int) remaining], 0, (int) remaining);
                remaining = 0;
            }
            return delegate.read(b, off, len);
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }
    }
}
