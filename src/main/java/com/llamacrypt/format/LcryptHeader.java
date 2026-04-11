package com.llamacrypt.format;

import com.llamacrypt.crypto.Argon2Params;

public final class LcryptHeader {
    public static final byte VERSION = 0x01;

    private final Argon2Params argon2Params;
    private final byte[] salt;
    private final byte[] iv;

    public LcryptHeader(Argon2Params argon2Params, byte[] salt, byte[] iv) {
        this.argon2Params = argon2Params;
        this.salt = salt.clone();
        this.iv = iv.clone();
    }

    public Argon2Params getArgon2Params() {
        return argon2Params;
    }

    public byte[] getSalt() {
        return salt.clone();
    }

    public byte[] getIv() {
        return iv.clone();
    }
}
