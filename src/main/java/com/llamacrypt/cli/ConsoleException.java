package com.llamacrypt.cli;

public class ConsoleException extends Exception {
    public ConsoleException(String message) {
        super(message);
    }

    public ConsoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
