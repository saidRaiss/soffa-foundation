package io.soffa.foundation.support.mail.adapters;

import io.soffa.foundation.commons.CollectionUtil;
import io.soffa.foundation.commons.TextUtil;
import io.soffa.foundation.exceptions.TechnicalException;
import io.soffa.foundation.models.mail.Email;
import io.soffa.foundation.models.mail.EmailAddress;
import io.soffa.foundation.models.mail.EmailId;
import io.soffa.foundation.support.mail.EmailSender;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;

@AllArgsConstructor
public class SmtpEmailSender implements EmailSender {

    private MailerConfig config;

    @SneakyThrows
    @Override
    public EmailId send(Email message) {
        check(message);
        HtmlEmail email = new HtmlEmail();
        email.setSmtpPort(config.getPort());
        if (config.hasCredentials()) {
            email.setAuthenticator(new DefaultAuthenticator(
                config.getUsername(),
                config.getPassword()
            ));
        }
        email.setDebug(false);
        email.setHostName("smtp.gmail.com");

        if (message.getSender() != null) {
            email.setFrom(message.getSender().getAddress(), message.getSender().getName());
        } else {
            email.setFrom(config.getSender());
        }
        email.setHtmlMsg(message.getHtmlMessage());
        email.setTextMsg(message.getTextMessage());
        email.setSubject(message.getSubject());

        for (EmailAddress to : message.getTo()) {
            email.addTo(to.getAddress(), to.getName());
        }
        for (EmailAddress cc : message.getCc()) {
            email.addCc(cc.getAddress(), cc.getName());
        }
        for (EmailAddress bcc : message.getBcc()) {
            email.addBcc(bcc.getAddress(), bcc.getName());
        }
        return new EmailId(email.send());
    }

    void check(Email message) {
        boolean hasSender = TextUtil.isNotEmpty(config.getSender()) || message.getSender() != null;

        if (!hasSender) {
            throw new TechnicalException("Missing sender (specify one in the message or in the global smtp config)");
        }
        if (TextUtil.isEmpty(message.getSubject())) {
            throw new TechnicalException("Email subject is required");
        }
        if (!message.hasMessage()) {
            throw new TechnicalException("Email content is required");
        }
        if (CollectionUtil.isEmpty(message.getTo())) {
            throw new TechnicalException("No recipients provided");
        }
    }

}
