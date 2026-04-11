package com.llamacrypt.cli;

import java.io.Console;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ArgumentParser {
    private static final String ENCRYPT_MODE = "encrypt";
    private static final String ENCRYPT_KEY_MODE = "encrypt-key";
    private static final String DECRYPT_MODE = "decrypt";

    public ParsedArgs parse(String[] args) {
        if (args == null || args.length == 0) {
            throw new InvalidArgumentException("Missing command. Usage: lcrypt <encrypt|encrypt-key|decrypt> <filename> [password]");
        }

        String modeArg = args[0].toLowerCase();
        OperationMode mode;

        switch (modeArg) {
            case ENCRYPT_MODE:
                mode = OperationMode.ENCRYPT;
                break;
            case ENCRYPT_KEY_MODE:
                mode = OperationMode.ENCRYPT_KEY;
                break;
            case DECRYPT_MODE:
                mode = OperationMode.DECRYPT;
                break;
            default:
                throw new InvalidArgumentException("Unknown command: " + modeArg + ". Use: encrypt, encrypt-key, or decrypt");
        }

        if (args.length < 2) {
            throw new InvalidArgumentException("Missing file argument. Usage: lcrypt <command> <filename> [password]");
        }

        Path sourceFile = Paths.get(args[1]).toAbsolutePath().normalize();

        if (!sourceFile.toString().endsWith(".lcrypt") && mode == OperationMode.DECRYPT) {
            throw new InvalidArgumentException("Decrypt command requires a .lcrypt file");
        }

        if (mode == OperationMode.ENCRYPT_KEY) {
            return ParsedArgs.forEncryptionWithKey(sourceFile);
        }

        char[] password;
        if (args.length >= 3) {
            password = args[2].toCharArray();
        } else {
            Console console = System.console();
            if (console == null) {
                throw new InvalidArgumentException("No console available. Please provide password as third argument.");
            }
            char[] readPassword = console.readPassword("Enter password: ");
            if (readPassword == null || readPassword.length == 0) {
                throw new InvalidArgumentException("Password cannot be empty");
            }
            password = readPassword;
        }

        if (password.length == 0) {
            throw new InvalidArgumentException("Password cannot be empty");
        }

        switch (mode) {
            case ENCRYPT:
                return ParsedArgs.forEncryption(sourceFile, password);
            case DECRYPT:
                return ParsedArgs.forDecryption(sourceFile, password);
            default:
                throw new InvalidArgumentException("Unknown operation mode");
        }
    }
}
