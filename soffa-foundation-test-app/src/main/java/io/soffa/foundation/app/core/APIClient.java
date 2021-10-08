package io.soffa.foundation.app.core;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIClient {

    @GET("ping")
    Call<PingResponse> ping();

}
