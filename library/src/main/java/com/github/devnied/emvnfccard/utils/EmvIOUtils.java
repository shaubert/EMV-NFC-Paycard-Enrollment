package com.github.devnied.emvnfccard.utils;

import java.io.Closeable;
import java.io.IOException;

public class EmvIOUtils {
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
        }
    }
}
