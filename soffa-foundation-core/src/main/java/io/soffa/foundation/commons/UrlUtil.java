package io.soffa.foundation.commons;

import lombok.SneakyThrows;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public final class UrlUtil {

    private UrlUtil() {
    }

    public static String join(String base, String... parts) {
        StringBuilder url = new StringBuilder(base.replaceAll("/+$", ""));
        for (String part : parts) {
            url.append('/').append(part.replaceAll("^/+", ""));
        }
        return url.toString();
    }

    @SneakyThrows
    public static UrlInfo parse(String value) {
        String[] parts = value.split("://");
        String protocol = parts[0];
        UrlInfo info = parse(new URL("https://" + parts[1]));
        info.setProtocol(protocol);
        return info;
    }

    @SneakyThrows
    public static UrlInfo parse(URL url) {
        String userInfos = url.getUserInfo();
        String username = null;
        String password = null;
        if (userInfos != null) {
            if (userInfos.contains(":")) {
                String[] userAndPassword = userInfos.trim().split(":");
                password = URLDecoder.decode(userAndPassword[1], StandardCharsets.UTF_8.toString());
                username = URLDecoder.decode(userAndPassword[0], StandardCharsets.UTF_8.toString());
            } else {
                username = userInfos.trim();
            }
        }
        return new UrlInfo(url.getProtocol(), url.getPort(), url.getHost(), username, password, url.getPath());
    }

}
