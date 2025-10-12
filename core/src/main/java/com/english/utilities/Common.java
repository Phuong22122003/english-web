package com.english.utilities;

import java.util.function.Supplier;

public class Common {
    public static <T> T getSafe(Supplier<T> supplier, T defaultValue) {
        try {
            T result = supplier.get();
            return result != null ? result : defaultValue;
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }
}
