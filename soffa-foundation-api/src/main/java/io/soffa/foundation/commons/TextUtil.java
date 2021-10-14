package io.soffa.foundation.commons;

import org.apache.commons.lang3.StringUtils;

public final class TextUtil {

    private TextUtil() {
    }

    public static boolean isEmpty(String... values) {
        for (String value : values) {
            if (StringUtils.isNotEmpty(value)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String... value) {
        for (String s : value) {
            if (StringUtils.isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    public static String trimToEmpty(String schema) {
        return StringUtils.trimToEmpty(schema);
    }

    public static String trimToNull(String schema) {
        return StringUtils.trimToNull(schema);
    }

    public static String format(String pattern, Object... args) {
        if (args == null || args.length == 0) {
            return pattern;
        }
        return String.format(pattern, args);
    }

}
