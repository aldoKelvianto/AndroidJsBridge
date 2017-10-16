package com.aldoapps.jsbridge.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by aldo on 16/10/17.
 */

public class IOUtil {

    private IOUtil() {
    }

    public static void closeQuietly(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable == null) break;

            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
