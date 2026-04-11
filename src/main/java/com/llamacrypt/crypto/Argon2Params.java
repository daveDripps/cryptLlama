package com.llamacrypt.crypto;

public final class Argon2Params {
    private final int memoryCostKiB;
    private final int iterations;
    private final int parallelism;

    public Argon2Params(int memoryCostKiB, int iterations, int parallelism) {
        this.memoryCostKiB = memoryCostKiB;
        this.iterations = iterations;
        this.parallelism = parallelism;
    }

    public static Argon2Params defaults() {
        return new Argon2Params(65536, 3, 4);
    }

    public int getMemoryCostKiB() {
        return memoryCostKiB;
    }

    public int getIterations() {
        return iterations;
    }

    public int getParallelism() {
        return parallelism;
    }

    public byte getVersion() {
        return (byte) 0x01;
    }
}
