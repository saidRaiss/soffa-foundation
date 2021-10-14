package io.soffa.foundation.s3;

import lombok.Data;

@Data
public class S3config {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;

}
