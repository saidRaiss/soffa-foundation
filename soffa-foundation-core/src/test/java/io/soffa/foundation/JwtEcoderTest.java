package io.soffa.foundation;

import com.google.common.collect.ImmutableMap;
import io.soffa.foundation.commons.jwt.DefaultJwtProcessor;
import io.soffa.foundation.commons.jwt.Jwt;
import io.soffa.foundation.commons.jwt.JwtEncoderConfig;
import io.soffa.foundation.core.model.Authentication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtEcoderTest {

    @Test
    public void testJwtEncoder() {
        JwtEncoderConfig config = new JwtEncoderConfig("test", "yujRAkZLDBW*Xaw3");
        DefaultJwtProcessor encoder = new DefaultJwtProcessor(config);

        Jwt jwt = encoder.create("Foundation", ImmutableMap.of("email", "foundation@soffa.io"));
        assertNotNull(jwt.getToken());
        assertEquals("Foundation", jwt.getSubject());

        Authentication auth = encoder.decode(jwt.getToken()).orElse(null);
        assertNotNull(auth);

    }

}
