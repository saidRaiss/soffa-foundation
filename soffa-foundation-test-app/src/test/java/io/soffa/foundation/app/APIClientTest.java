package io.soffa.foundation.app;

import io.soffa.foundation.app.core.PingResponse;
import io.soffa.foundation.app.gateway.API;
import io.soffa.foundation.client.RestClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIClientTest {

    @LocalServerPort
    private int port;

    @SneakyThrows
    @Test
    public void testAPIClient() {
        API client = RestClient.newInstance(API.class, "http://localhost:" + port);
        PingResponse response = client.ping();
        assertEquals("PONG", response.getValue());
        assertEquals("Hello", client.echo("Hello"));
    }
}
