package io.soffa.foundation.commons.jwt;


import java.util.Map;

public interface JwtEncoder {

    Jwt create(String subjet, Map<String, Object> claims);

    Jwt create(String subjet, Map<String, Object> claims, int ttl);
}
