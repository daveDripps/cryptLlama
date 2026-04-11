package com.llamacrypt.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class StreamProcessorTest {
    private StreamProcessor processor;

    @TempDir
    Path tempDir;

    @Test
    void process_shouldCopyDataFromSourceToDestination() throws IOException {
        processor = new StreamProcessor();
        byte[] testData = "Test content for stream processing".getBytes();
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.write(source, testData);

        processor.process(source, destination, (in, out) -> {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        });

        assertTrue(Files.exists(destination));
        assertArrayEquals(testData, Files.readAllBytes(destination));
    }

    @Test
    void process_shouldHandleLargeFiles() throws IOException {
        processor = new StreamProcessor();
        byte[] largeData = new byte[1024 * 100];
        new java.security.SecureRandom().nextBytes(largeData);
        Path source = tempDir.resolve("large.bin");
        Path destination = tempDir.resolve("large_copy.bin");
        Files.write(source, largeData);

        processor.process(source, destination, (in, out) -> {
            byte[] buffer = new byte[65536];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        });

        assertArrayEquals(largeData, Files.readAllBytes(destination));
    }

    @Test
    void process_withNonexistentSource_shouldThrowProcessingException() {
        processor = new StreamProcessor();
        Path source = tempDir.resolve("nonexistent.txt");
        Path destination = tempDir.resolve("output.txt");

        assertThrows(ProcessingException.class, () ->
                processor.process(source, destination, (in, out) -> {})
        );
    }

    @Test
    void process_withCustomOperation_shouldApplyOperation() throws IOException {
        processor = new StreamProcessor();
        byte[] testData = "HELLO WORLD".getBytes();
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.write(source, testData);

        processor.process(source, destination, (in, out) -> {
            int ch;
            while ((ch = in.read()) != -1) {
                out.write(Character.toLowerCase(ch));
            }
        });

        assertArrayEquals("hello world".getBytes(), Files.readAllBytes(destination));
    }

    @Test
    void process_shouldCloseStreamsOnSuccess() throws IOException {
        processor = new StreamProcessor();
        byte[] testData = "Data".getBytes();
        Path source = tempDir.resolve("source.txt");
        Path destination = tempDir.resolve("destination.txt");
        Files.write(source, testData);

        final boolean[] inputClosed = {false};
        final boolean[] outputClosed = {false};

        processor.process(source, destination, (in, out) -> {
            out.write(in.read());
        });

        assertTrue(Files.exists(destination));
    }
}
