package io.soffa.foundation.app;

import io.soffa.foundation.actions.ActionDispatcher;
import io.soffa.foundation.context.TenantHolder;
import io.soffa.foundation.core.model.TenantId;
import io.soffa.foundation.data.SysLogRepository;
import io.soffa.foundation.events.Event;
import io.soffa.foundation.exceptions.FakeException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
    public void testSysAction() {

        TenantId t1 = new TenantId("T1");
        TenantId t2 = new TenantId("T2");

        AtomicLong t1InitialCount = new AtomicLong();

        String actionName = "PingAction";
        TenantHolder.run(t1, () -> {
            t1InitialCount.set(sysLogs.count());
            dispatcher.handle(new Event(actionName)); // automatic tenant
            dispatcher.handle(new Event("EchoAction", "Hello"));
            dispatcher.handle(new Event(actionName).withTenant(t1)); // explicit tenant
        });

        AtomicLong t2InitialCount = new AtomicLong();

        TenantHolder.run(t2, () -> {
            t2InitialCount.set(sysLogs.count());
            Assertions.assertThrows(FakeException.class, () -> {
                dispatcher.handle(new Event(actionName));
            });
        });

        TenantHolder.run(t1, () -> {
            assertEquals(t1InitialCount.get() + 3, sysLogs.count());
        });

        TenantHolder.run(t2, () -> {
            assertEquals(t2InitialCount.get() + 1, sysLogs.count());
        });
    }


}
