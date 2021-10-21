package io.soffa.foundation.commons.jwt;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtEncoderConfig {

    private String issuer;
    private String secret;
    private int defaultTtl = 60;

    public JwtEncoderConfig(String issuer, String secret) {
        this.issuer = issuer;
        this.secret = secret;
    }

}
