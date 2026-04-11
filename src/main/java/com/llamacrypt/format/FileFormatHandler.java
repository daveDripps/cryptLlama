package com.llamacrypt.format;

import com.llamacrypt.crypto.Argon2Params;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public final class FileFormatHandler {
    private static final byte[] MAGIC_BYTES = {'L', 'C', 'R', 'Y'};
    private static final int HEADER_SIZE = 42;

    public void writeHeader(OutputStream out, LcryptHeader header) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);

            buffer.put(MAGIC_BYTES);
            buffer.put(header.getArgon2Params().getVersion());
            buffer.putInt(header.getArgon2Params().getMemoryCostKiB());
            buffer.putInt(header.getArgon2Params().getIterations());
            buffer.put((byte) header.getArgon2Params().getParallelism());
            buffer.put(header.getSalt());
            buffer.put(header.getIv());

            out.write(buffer.array());
            out.flush();
        } catch (IOException e) {
            throw new FormatException("Failed to write file header", e);
        }
    }

    public LcryptHeader readHeader(InputStream in) {
        try {
            byte[] headerBytes = new byte[HEADER_SIZE];
            int bytesRead = readFully(in, headerBytes);
            if (bytesRead < HEADER_SIZE) {
                throw new FormatException("File is too small to be a valid encrypted file");
            }

            ByteBuffer buffer = ByteBuffer.wrap(headerBytes);

            byte[] magic = new byte[4];
            buffer.get(magic);
            if (!java.util.Arrays.equals(magic, MAGIC_BYTES)) {
                throw new FormatException("File is not a valid encrypted file");
            }

            byte version = buffer.get();
            if (version != LcryptHeader.VERSION) {
                throw new FormatException("File was encrypted with an unsupported version");
            }

            int memoryCost = buffer.getInt();
            int iterations = buffer.getInt();
            int parallelism = buffer.get() & 0xFF;
            Argon2Params argon2Params = new Argon2Params(memoryCost, iterations, parallelism);

            byte[] salt = new byte[16];
            buffer.get(salt);

            byte[] iv = new byte[12];
            buffer.get(iv);

            return new LcryptHeader(argon2Params, salt, iv);
        } catch (FormatException e) {
            throw e;
        } catch (Exception e) {
            throw new FormatException("Failed to read file header", e);
        }
    }

    private int readFully(InputStream in, byte[] buffer) throws IOException {
        int totalRead = 0;
        while (totalRead < buffer.length) {
            int bytesRead = in.read(buffer, totalRead, buffer.length - totalRead);
            if (bytesRead == -1) {
                break;
            }
            totalRead += bytesRead;
        }
        return totalRead;
    }
}
