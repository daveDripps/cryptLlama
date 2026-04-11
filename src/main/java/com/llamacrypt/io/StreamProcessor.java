package com.llamacrypt.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class StreamProcessor {
    public void process(Path source, Path destination, StreamOperation operation) {
        try (InputStream in = Files.newInputStream(source);
             OutputStream out = Files.newOutputStream(destination,
                     StandardOpenOption.CREATE,
                     StandardOpenOption.TRUNCATE_EXISTING)) {
            operation.apply(in, out);
        } catch (IOException e) {
            throw new ProcessingException("Failed to process file", e);
        }
    }
}
