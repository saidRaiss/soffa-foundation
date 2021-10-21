package io.soffa.foundation.commons.jwt;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWEDecryptionKeySelector;
import com.nimbusds.jose.proc.JWEKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import io.soffa.foundation.commons.Logger;
import io.soffa.foundation.core.model.Authentication;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
public class DefaultJwtEncoder implements JwtEncoder, JwtDecoder {

    private static final Logger LOG = Logger.get(JwtJwksDecoder.class);
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

    @Override
    public Optional<Authentication> decode(String jwt) {
        try {

            byte[] secretKey = config.getSecret().getBytes();
            // AESEncrypter encrypter = new AESEncrypter(secretKey);
            ConfigurableJWTProcessor<SimpleSecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            JWKSource<SimpleSecurityContext> jweKeySource = new ImmutableSecret<>(secretKey);
            JWEKeySelector<SimpleSecurityContext> jweKeySelector =
                new JWEDecryptionKeySelector<>(JWEAlgorithm.A128KW, EncryptionMethod.A128CBC_HS256, jweKeySource);
            jwtProcessor.setJWEKeySelector(jweKeySelector);

            JWTClaimsSet claimsSet = jwtProcessor.process(jwt, null);
            return Optional.of(extractInfo(new Jwt(jwt, claimsSet.getSubject(), claimsSet.getClaims())));
        } catch (Exception e) {
            LOG.error(e);
            return Optional.empty();
        }
    }
}
