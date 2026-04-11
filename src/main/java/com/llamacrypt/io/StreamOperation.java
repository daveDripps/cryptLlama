package com.llamacrypt.io;

import java.io.InputStream;
import java.io.OutputStream;

@FunctionalInterface
public interface StreamOperation {
    void apply(InputStream in, OutputStream out) throws java.io.IOException;
}
