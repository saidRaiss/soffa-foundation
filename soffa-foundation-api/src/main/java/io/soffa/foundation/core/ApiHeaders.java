package io.soffa.foundation.core;

public interface ApiHeaders {

    String TENANT_ID = "X-TenantId";
    String APPLICATION = "X-Application";
    String TRACE_ID = "X-TraceId";
    String SPAN_ID = "X-SpanId";

    @Deprecated
    String JWT = "BEARER_AUTH";
    @Deprecated
    String BASIC_AUTH = "BASIC_AUTH";
    @Deprecated
    String OAUTH2 = "OAUTH2";
}
