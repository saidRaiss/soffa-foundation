package io.soffa.foundation.app;

import io.soffa.foundation.context.TenantHolder;
import io.soffa.foundation.core.ApiHeaders;
import io.soffa.foundation.data.SysLogRepository;
import io.soffa.foundation.test.HttpExpect;
import io.soffa.foundation.test.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(properties = {"app.sys-logs.enabled=true"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SysLogTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SysLogRepository sysLogs;

    @Test
    public void testSysAction() {
        TenantHolder.set("T1");
        HttpExpect test = new HttpExpect(mvc);

        long initialCount = sysLogs.count();
        test.get("/ping").withTenant("T1").expect().isOK().json("$.value", "PONG");

        TestUtil.awaitUntil(3, () -> initialCount + 1 == sysLogs.count());

        test.get("/ping").
            withTenant("T1").
            header(ApiHeaders.APPLICATION, "Demo").
            expect().isOK().json("$.value", "PONG");

        TestUtil.awaitUntil(3, () -> initialCount + 2 == sysLogs.count());
    }


}
