package io.soffa.foundation.commons;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class ExecutorHelper {

    private static final ExecutorService SC = Executors.newFixedThreadPool(16);

    private ExecutorHelper() {}

    public static Future<?> submit(final Runnable runnable) {
        return SC.submit(runnable);
    }

    public static void execute(final Runnable runnable) {
        SC.execute(runnable);
    }

}
