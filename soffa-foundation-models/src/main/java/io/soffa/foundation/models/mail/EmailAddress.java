package io.soffa.foundation.models.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAddress {

    private String name;
    private String address;

    public EmailAddress(String value) {
        if (value.contains("<")) {
            String[] parts = value.trim().split("<");
            name = parts[0].trim().replaceAll("^\"|\"$", "").trim();
            address = parts[1].split(">")[0];
        } else {
            address = value.trim();
        }
    }

    public static List<EmailAddress> from(List<String> addresses) {
        return addresses.stream().map(EmailAddress::new).collect(Collectors.toList());
    }

}
