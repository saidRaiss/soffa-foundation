package io.soffa.foundation.commons.jwt;


import lombok.Data;

@Data
public class JwtEncoderConfig {

    private String issuer;
    private String secret;
    private int defaultTtl;

}
