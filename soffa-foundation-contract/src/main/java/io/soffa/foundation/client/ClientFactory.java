package io.soffa.foundation.client;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class ClientFactory {

    private ClientFactory() {}

    public static <T> T newClient(Class<T> clientInterface, String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
        return retrofit.create(clientInterface);
    }

}
