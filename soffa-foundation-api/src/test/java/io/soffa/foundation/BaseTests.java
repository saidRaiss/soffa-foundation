package io.soffa.foundation;

import io.soffa.foundation.commons.ClassUtil;
import io.soffa.foundation.core.model.TenantId;
import io.soffa.foundation.model.PagedResult;
import io.soffa.foundation.model.User;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BaseTests {

    @Test
    public void testTenantId() {
        TenantId t = new TenantId("T1");
        assertEquals("T1", t.getValue());
        assertEquals(new TenantId("T1"), t);
    }

    //@Test
    public void testParametrizedType() {
        ParameterizedType type = ClassUtil.constructParametricType(PagedResult.class, User.class);
        assertNotNull(type);
    }


}
