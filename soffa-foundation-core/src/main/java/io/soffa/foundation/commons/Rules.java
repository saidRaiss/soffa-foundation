package io.soffa.foundation.commons;

import io.soffa.foundation.exceptions.FunctionalException;
import java.util.function.Supplier;

public class Rules {

    private Rules() {
    }

    public static void check(String message, Supplier<Boolean> supplier) {
        if (!supplier.get()) {
            throw new FunctionalException(message);
        }
    }
}
