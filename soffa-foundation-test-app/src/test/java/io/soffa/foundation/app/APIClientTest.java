package io.soffa.foundation.app;

import io.soffa.foundation.app.core.PingResponse;
import io.soffa.foundation.app.gateway.API;
import io.soffa.foundation.client.RestClient;
import io.soffa.foundation.core.RequestContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIClientTest {

    @LocalServerPort
    private int port;

    @SneakyThrows
    @Test
    public void testAPIClient() {
        API client = RestClient.newInstance(API.class, "http://localhost:" + port);
        RequestContext context = RequestContext.create("T1");
        PingResponse response = client.ping(context);
        assertEquals("PONG", response.getValue());
        assertEquals("Hello", client.echo("Hello", context));
    }
}
