package io.soffa.foundation.commons;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectUtil {

    private static final ObjectMapper MAPPER = ObjectFactory.create(new ObjectMapper());

    private ObjectUtil() {
    }

    public static <T> T convert(Object input, Class<T> type) {
        return ObjectFactory.convert(MAPPER, input, type);
    }

}
