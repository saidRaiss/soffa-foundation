package io.soffa.foundation.support.mail.adapters;

import io.soffa.foundation.commons.TextUtil;
import lombok.Data;

@Data
public class MailerConfig {

    private int port = 25;
    private String provider;
    private String server;
    private String username;
    private String password;
    private String sender;

    public boolean hasCredentials() {
        return TextUtil.isNotEmpty(username);
    }

    public void afterPropertiesSet() {
        if (TextUtil.isEmpty(server)) {
            return;
        }
        if (server.contains(":")) {
            String[] parts = server.split(":");
            server = parts[0];
            port = Integer.parseInt(parts[1]);
        }
    }
}
