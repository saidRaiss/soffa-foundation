package io.soffa.foundation.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Value;

@Value
public class TenantId {

    public static final String DEFAULT = "default";

    @JsonValue
    String value;

    @JsonCreator
    public TenantId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
