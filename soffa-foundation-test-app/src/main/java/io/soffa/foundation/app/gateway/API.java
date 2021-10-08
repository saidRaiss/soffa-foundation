package io.soffa.foundation.app.gateway;

import io.soffa.foundation.app.core.PingResponse;
import io.soffa.foundation.core.ApiHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import retrofit2.http.GET;

@Tags(
    @Tag(name = "app", description = "Default application tag")
)
public interface API {

    @Operation(
        summary = "Ping endpoint",
        description = "Will return pong message on successful request",
        parameters = {@Parameter(ref = ApiHeaders.TENANT_ID)}
    )
    @GET("ping")
    PingResponse ping();

}
