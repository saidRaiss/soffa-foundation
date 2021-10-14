package io.soffa.foundation.commons;

import lombok.Data;

@Data
public class UrlInfo {

    private String protocol;
    private int port;
    private String hostname;
    private String username;
    private String password;

}
