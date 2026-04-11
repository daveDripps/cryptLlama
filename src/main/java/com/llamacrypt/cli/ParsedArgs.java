package com.llamacrypt.cli;

import java.nio.file.Path;

public final class ParsedArgs {
    private final OperationMode mode;
    private final Path sourceFile;
    private final char[] password;

    private ParsedArgs(OperationMode mode, Path sourceFile, char[] password) {
        this.mode = mode;
        this.sourceFile = sourceFile;
        this.password = password;
    }

    public static ParsedArgs forEncryption(Path sourceFile, char[] password) {
        return new ParsedArgs(OperationMode.ENCRYPT, sourceFile, password);
    }

    public static ParsedArgs forEncryptionWithKey(Path sourceFile) {
        return new ParsedArgs(OperationMode.ENCRYPT_KEY, sourceFile, null);
    }

    public static ParsedArgs forDecryption(Path sourceFile, char[] password) {
        return new ParsedArgs(OperationMode.DECRYPT, sourceFile, password);
    }

    public OperationMode getMode() {
        return mode;
    }

    public Path getSourceFile() {
        return sourceFile;
    }

    public char[] getPassword() {
        return password;
    }

    public boolean hasPassword() {
        return password != null;
    }
}
