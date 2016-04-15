package com.github.devnied.emvnfccard.utils;

import android.util.Log;

public class Logger {

    public static boolean ENABLED = true;

    public static void e(String tag, String message) {
        if (ENABLED) Log.e(tag, message);
    }

    public static void e(String tag, String message, Exception ex) {
        if (ENABLED) Log.e(tag, message, ex);
    }

}
