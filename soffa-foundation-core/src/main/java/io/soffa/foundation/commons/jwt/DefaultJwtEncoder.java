package io.soffa.foundation.commons.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class DefaultJwtEncoder implements JwtEncoder {

    private JwtEncoderConfig config;

    @Override
    public Jwt create(String subjet, Map<String, Object> claims) {
        return create(subjet, claims, config.getDefaultTtl());
    }

    @Override
    public Jwt create(String subjet, Map<String, Object> claims, int ttl) {
        String token = JwtUtil.create(
            config.getIssuer(),
            config.getSecret(),
            subjet,
            claims,
            ttl
        );
        return new Jwt(token, subjet, claims, ttl);
    }

}
