package com.llamacrypt.service;

public final class DecryptionResult {
    private final String outputPath;

    private DecryptionResult(String outputPath) {
        this.outputPath = outputPath;
    }

    public static DecryptionResult success(String outputPath) {
        return new DecryptionResult(outputPath);
    }

    public String getOutputPath() {
        return outputPath;
    }
}
