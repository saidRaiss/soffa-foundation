package io.soffa.foundation.models.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ack {

    private String value;
    private boolean success;

    public Ack(boolean success) {
        this.success = success;
    }

    public Ack(String value) {
        this.value = value;
    }
}
