package io.soffa.foundation.context;

import com.google.common.base.Preconditions;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.core.model.TenantId;
import io.soffa.foundation.exceptions.FunctionalException;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class TenantHolder {

    private static final ThreadLocal<String> CURRENT = new InheritableThreadLocal<>();
    public static boolean hasDefault;


    private TenantHolder() {
    }

    public static void set(TenantId tenantId) {
        Preconditions.checkNotNull(tenantId, "Tenant cannot be empty");
        Logger.setTenantId(tenantId);
        set(tenantId.getValue());
    }

    public static void set(String value) {
        Preconditions.checkArgument(TextUtil.isNotEmpty(value), "Tenant cannot be empty");
        synchronized (CURRENT) {
            if (TextUtil.isEmpty(value)) {
                CURRENT.remove();
            } else {
                CURRENT.set(value);
            }
        }
    }

    public static void clear() {
        synchronized (CURRENT) {
            CURRENT.remove();
        }
    }

    public static boolean isEmpty() {
        return TextUtil.isEmpty(CURRENT.get()) && !hasDefault;
    }

    public static Optional<String> get() {
        return Optional.ofNullable(CURRENT.get());
    }

    public static String require() {
        if (CURRENT.get() == null) {
            if (hasDefault) {
                return TenantId.DEFAULT_VALUE;
            }
            throw new FunctionalException("MISSING_TENANT");
        }
        return CURRENT.get();
    }

    @SneakyThrows
    public static void use(final TenantId tenantId, Runnable runnable) {
        if (tenantId == null) {
            runnable.run();
        } else {
            String current = CURRENT.get();
            try {
                set(tenantId.getValue());
                runnable.run();
            } finally {
                set(current);
            }
        }
    }


    @SneakyThrows
    public static <O> O use(final TenantId tenantId, Supplier<O> supplier) {
        if (tenantId == null) {
            return supplier.get();
        } else {
            String current = CURRENT.get();
            try {
                set(tenantId.getValue());
                return supplier.get();
            } finally {
                set(current);
            }
        }
    }

    @SneakyThrows
    public static void use(final String tenantId, Consumer<TenantId> consumer) {
        use(TenantId.of(tenantId), consumer);
    }

    public static void use(final TenantId tenantId, Consumer<TenantId> consumer) {
        set(tenantId);
        consumer.accept(tenantId);
    }

    @SneakyThrows
    public static void submit(final String tenantId, Consumer<TenantId> consumer) {
        submit(TenantId.of(tenantId), consumer);
    }

    @SneakyThrows
    public static void submit(final TenantId tenantId, Runnable runnable) {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                set(tenantId);
                runnable.run();
            } finally {
                latch.countDown();

            }
        }).start();
        latch.await();
    }

    @SneakyThrows
    public static void submit(final TenantId tenantId, Consumer<TenantId> consumer) {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                set(tenantId);
                consumer.accept(tenantId);
            } finally {
                latch.countDown();

            }
        }).start();
        latch.await();
    }
}
