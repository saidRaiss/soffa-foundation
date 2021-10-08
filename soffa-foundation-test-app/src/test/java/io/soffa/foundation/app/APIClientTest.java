package io.soffa.foundation.app;

import io.soffa.foundation.app.core.APIClient;
import io.soffa.foundation.app.core.PingResponse;
import io.soffa.foundation.client.ClientFactory;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import retrofit2.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIClientTest {

    @LocalServerPort
    private int port;

    @SneakyThrows
    @Test
    public void testAPIClient() {
        APIClient client = ClientFactory.newClient(APIClient.class, "http://localhost:" + port);
        Response<PingResponse> response = client.ping().execute();
        assertEquals(200, response.code());
        PingResponse result = response.body();
        assertNotNull(result);
        assertEquals("PONG", result.getValue());
    }
}
