package io.soffa.foundation.app.events;

import io.soffa.foundation.actions.ActionDispatcher;
import io.soffa.foundation.context.TenantHolder;
import io.soffa.foundation.data.SysLogRepository;
import io.soffa.foundation.events.Event;
import io.soffa.foundation.exceptions.FakeException;
import lombok.SneakyThrows;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"app.sys-logs.enabled=true"})
@ActiveProfiles("test")
public class EventHandleTest {

    @Autowired
    private ActionDispatcher dispatcher;

    @Autowired
    private SysLogRepository sysLogs;

    @SneakyThrows
    @Test
    public void testActionDispatcher() {

        String actionName = "PingAction";

        TenantHolder.use("T1", (t1) -> {
            long initialCount = sysLogs.count();
            dispatcher.handle(new Event(actionName)); // automatic tenant
            dispatcher.handle(new Event("EchoAction", "Hello"));
            dispatcher.handle(new Event(actionName).withTenant(t1)); // explicit tenant
            Awaitility.await().atMost(500, TimeUnit.MILLISECONDS).until(() -> sysLogs.count() == initialCount + 3);
        });

        final AtomicLong t2InitialCount = new AtomicLong();

        TenantHolder.use("T2", (t2) -> {
            t2InitialCount.set(sysLogs.count());
            Assertions.assertThrows(FakeException.class, () -> dispatcher.handle(new Event(actionName)));
        });

        TenantHolder.use("T2", () -> {
            Awaitility.await().atMost(500, TimeUnit.MILLISECONDS).until(() -> t2InitialCount.get() + 1 == sysLogs.count());
            assertEquals(t2InitialCount.get() + 1, sysLogs.count());
        });

    }


}
