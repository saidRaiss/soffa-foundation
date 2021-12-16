package io.soffa.foundation.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlInfo {

    private String protocol;
    private int port;
    private String hostname;
    private String username;
    private String password;
    private String path;
    private static final Map<String,Integer> DEFAULT_PORTS = new HashMap<>();
    static {
        DEFAULT_PORTS.put("http", 80);
        DEFAULT_PORTS.put("https", 443);
        DEFAULT_PORTS.put("amqp", 5672);
        DEFAULT_PORTS.put("amqps", 5672);
    }

    public String getAddress() {
        if (isDefaultPort()) {
            return protocol + "://" + hostname;
        }
        return protocol + "://" + hostname + ":" + port;
    }

    public String getHostnameWithPort() {
        if (port > 0) {
            return hostname + ":" + port;
        }
        return hostname;
    }

    private boolean isDefaultPort() {
        if (port == 0) return true;
        for (String protocol : DEFAULT_PORTS.keySet()) {
            if (protocol.equalsIgnoreCase(this.protocol)) {
                return true;
            }
        }
        return false;
    }


}
