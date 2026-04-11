package com.llamacrypt.crypto;

public class KeyDerivationException extends RuntimeException {
    public KeyDerivationException(String message) {
        super(message);
    }

    public KeyDerivationException(String message, Throwable cause) {
        super(message, cause);
    }
}
