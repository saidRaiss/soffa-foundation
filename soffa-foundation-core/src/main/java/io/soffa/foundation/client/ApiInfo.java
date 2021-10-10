package io.soffa.foundation.client;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiInfo {

    private String method;
    private String path;

}
