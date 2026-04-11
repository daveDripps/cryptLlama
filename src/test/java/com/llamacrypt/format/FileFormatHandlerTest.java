package com.llamacrypt.format;

import com.llamacrypt.crypto.Argon2Params;
import com.llamacrypt.crypto.KeyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class FileFormatHandlerTest {
    private FileFormatHandler handler;
    private KeyManager keyManager;

    @BeforeEach
    void setUp() {
        handler = new FileFormatHandler();
        keyManager = new KeyManager();
    }

    @Test
    void writeReadHeader_shouldRoundTrip() {
        Argon2Params params = Argon2Params.defaults();
        byte[] salt = keyManager.generateSalt();
        byte[] iv = keyManager.generateIv();
        LcryptHeader original = new LcryptHeader(params, salt, iv);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        handler.writeHeader(out, original);

        byte[] headerBytes = out.toByteArray();
        assertEquals(42, headerBytes.length);

        ByteArrayInputStream in = new ByteArrayInputStream(headerBytes);
        LcryptHeader read = handler.readHeader(in);

        assertEquals(params.getMemoryCostKiB(), read.getArgon2Params().getMemoryCostKiB());
        assertEquals(params.getIterations(), read.getArgon2Params().getIterations());
        assertEquals(params.getParallelism(), read.getArgon2Params().getParallelism());
        assertArrayEquals(salt, read.getSalt());
        assertArrayEquals(iv, read.getIv());
    }

    @Test
    void readHeader_withInvalidMagic_shouldThrowFormatException() {
        byte[] invalidHeader = new byte[42];
        invalidHeader[0] = 'X';
        invalidHeader[1] = 'X';
        invalidHeader[2] = 'X';
        invalidHeader[3] = 'X';

        ByteArrayInputStream in = new ByteArrayInputStream(invalidHeader);
        assertThrows(FormatException.class, () -> handler.readHeader(in));
    }

    @Test
    void readHeader_withInvalidVersion_shouldThrowFormatException() {
        byte[] header = new byte[42];
        header[0] = 'L';
        header[1] = 'C';
        header[2] = 'R';
        header[3] = 'Y';
        header[4] = (byte) 0x99;

        ByteArrayInputStream in = new ByteArrayInputStream(header);
        assertThrows(FormatException.class, () -> handler.readHeader(in));
    }

    @Test
    void readHeader_withTruncatedData_shouldThrowFormatException() {
        byte[] truncated = new byte[10];

        ByteArrayInputStream in = new ByteArrayInputStream(truncated);
        assertThrows(FormatException.class, () -> handler.readHeader(in));
    }

    @Test
    void readHeader_withCustomParams_shouldPreserveValues() {
        Argon2Params customParams = new Argon2Params(32768, 5, 2);
        byte[] salt = keyManager.generateSalt();
        byte[] iv = keyManager.generateIv();
        LcryptHeader original = new LcryptHeader(customParams, salt, iv);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        handler.writeHeader(out, original);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        LcryptHeader read = handler.readHeader(in);

        assertEquals(32768, read.getArgon2Params().getMemoryCostKiB());
        assertEquals(5, read.getArgon2Params().getIterations());
        assertEquals(2, read.getArgon2Params().getParallelism());
    }
}
