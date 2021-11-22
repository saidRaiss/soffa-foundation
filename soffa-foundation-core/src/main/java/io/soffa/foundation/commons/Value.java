package io.soffa.foundation.commons;

import java.util.Optional;

public class Value {

    private Value() {}

    public static <T> T getOrElse(T value, T defaultValue) {
        if (value instanceof String) {
            if (TextUtil.isEmpty((String)value)) {
                return defaultValue;
            }
            return value;
        }
        return Optional.ofNullable(value).orElse(defaultValue);
    }

}
