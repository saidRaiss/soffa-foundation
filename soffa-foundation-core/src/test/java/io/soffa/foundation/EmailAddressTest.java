package io.soffa.foundation;

import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.models.mail.EmailAddress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmailAddressTest {

    @Test
    public void testEmailAddress() {

        String address = "noreply@email.local";

        EmailAddress email = new EmailAddress(" noreply@email.local ");
        assertNull(email.getName());
        assertEquals(address, email.getAddress());

        email = new EmailAddress(" <noreply@email.local> ");
        assertTrue(TextUtil.isEmpty(email.getName()));
        assertEquals(address, email.getAddress());

        email = new EmailAddress("John Doe <noreply@email.local> ");
        assertEquals("John Doe", email.getName());
        assertEquals(address, email.getAddress());

        email = new EmailAddress("\"John Doe\" <noreply@email.local> ");
        assertEquals("John Doe", email.getName());
        assertEquals(address, email.getAddress());

    }

}
